package github.oldLab.oldLab.serviceImpl;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import github.oldLab.oldLab.exception.InvalidTokenException;
import github.oldLab.oldLab.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.secret}")
    private String KEY;
    
    @Value("${jwt.private-key:}")
    private String jwtPrivateKeyPath;

    @Value("${jwt.public-key:}")
    private String jwtPublicKeyPath;

    // cached keys
    private volatile KeyPair rsaKeyPair;
    private volatile PrivateKey privateKey;
    private volatile PublicKey publicKey;
        
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("failed to extract username from token", e);
            throw new InvalidTokenException("failed to extract username from token: " + token + "\n" + e.getMessage());
        }
    }
    
    @Async("asyncExecutor")
    public CompletableFuture<Claims> extractAllClaimsAsync(String token) {
        try {
        Jws<Claims> jws = Jwts.parser()
            .verifyWith(getPublicKey())
            .build()
            .parseSignedClaims(token);
        Claims claims = jws.getPayload();
            return CompletableFuture.completedFuture(claims);
        } catch (Exception e) {
            log.error("token validation failed: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    private PrivateKey loadPrivateKeyFromClasspath(String path) throws Exception {
        if (path == null || path.isBlank()) return null;
        String cp = path.startsWith("classpath:") ? path.substring("classpath:".length()) : path;
        Resource res = new ClassPathResource(cp);
        byte[] bytes = res.getInputStream().readAllBytes();
        String pem = new String(bytes, StandardCharsets.UTF_8)
                .replaceAll("-----BEGIN [^-]+-----", "")
                .replaceAll("-----END [^-]+-----", "")
                .replaceAll("\n", "")
                .replaceAll("\r", "");
        byte[] decoded = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private PublicKey loadPublicKeyFromClasspath(String path) throws Exception {
        if (path == null || path.isBlank()) return null;
        String cp = path.startsWith("classpath:") ? path.substring("classpath:".length()) : path;
        Resource res = new ClassPathResource(cp);
        byte[] bytes = res.getInputStream().readAllBytes();
        String pem = new String(bytes, StandardCharsets.UTF_8)
                .replaceAll("-----BEGIN [^-]+-----", "")
                .replaceAll("-----END [^-]+-----", "")
                .replaceAll("\n", "")
                .replaceAll("\r", "");
        byte[] decoded = Base64.getDecoder().decode(pem);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    private KeyPair getOrGenerateKeyPair() {
        if (rsaKeyPair != null) return rsaKeyPair;
        synchronized (this) {
            if (rsaKeyPair != null) return rsaKeyPair;
            // try load from configured PEMs
            try {
                if (jwtPrivateKeyPath != null && !jwtPrivateKeyPath.isBlank()) {
                    privateKey = loadPrivateKeyFromClasspath(jwtPrivateKeyPath);
                }
                if (jwtPublicKeyPath != null && !jwtPublicKeyPath.isBlank()) {
                    publicKey = loadPublicKeyFromClasspath(jwtPublicKeyPath);
                }
                if (privateKey != null && publicKey != null) {
                    rsaKeyPair = new KeyPair(publicKey, privateKey);
                    log.info("Loaded RSA keys from classpath: {} and {}", jwtPublicKeyPath, jwtPrivateKeyPath);
                    return rsaKeyPair;
                }
            } catch (Exception e) {
                log.warn("Failed to load RSA keys from classpath, will generate a temporary pair: {}", e.getMessage());
            }
            // fallback: generate ephemeral RSA keypair
            try {
                java.security.KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                rsaKeyPair = kpg.generateKeyPair();
                privateKey = rsaKeyPair.getPrivate();
                publicKey = rsaKeyPair.getPublic();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to generate RSA key pair", e);
            }
            log.info("Generated ephemeral RSA key pair for JWT RS256");
            return rsaKeyPair;
        }
    }

    private PrivateKey getPrivateKey() {
        if (privateKey != null) return privateKey;
        return getOrGenerateKeyPair().getPrivate();
    }

    private PublicKey getPublicKey() {
        if (publicKey != null) return publicKey;
        return getOrGenerateKeyPair().getPublic();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final CompletableFuture<Claims> claim = extractAllClaimsAsync(token);
        return claim.thenApply(claimsResolver).join();
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> generateTokenAsync(Map<String, Object> extraClaims, UserDetails userDetails){
        log.info("generating token for user: {}", userDetails.getUsername());
        extraClaims.put("roles", userDetails.getAuthorities().stream()
        .map(authority -> authority.getAuthority().replace("ROLE_", ""))
        .collect(Collectors.toList()));
            try {
                log.debug("extraClaims: {}", extraClaims);
                String token = Jwts.builder()
                    .claims(extraClaims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 25)) //25 minutes expiration
                    .signWith(getPrivateKey())
                    .compact();
            return CompletableFuture.completedFuture(token);
        } catch (Exception e) {
            log.info("failed to generate token for user: {}", userDetails.getUsername(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> generateToken(UserDetails userDetails){
        return generateTokenAsync(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
