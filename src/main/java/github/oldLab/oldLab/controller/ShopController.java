package github.oldLab.oldLab.controller;

import github.oldLab.oldLab.Enum.CategoryEnum;
import github.oldLab.oldLab.dto.request.ShopRequest;
import github.oldLab.oldLab.dto.response.ShopResponse;
import github.oldLab.oldLab.service.ShopService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shops")
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/async/create")
    public ResponseEntity<Void> createShop(@RequestBody ShopRequest shopRequest,
                                           @RequestHeader("Authorization") String header,
                                           HttpServletRequest httpRequest) {
    
        log.debug("Received request to create shop: {}", shopRequest);
        String token = header.substring(7);
        shopService.createShopAsync(shopRequest, token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopResponse> getShop(@PathVariable Long id, HttpServletRequest httpRequest) {
        
            ShopResponse shopResponse = shopService.getShop(id);
            return ResponseEntity.ok(shopResponse);
    }

    @GetMapping
    public ResponseEntity<List<ShopResponse>> getAllShops(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
            List<ShopResponse> shops = shopService.getAllShopsPaginated(page, size);
            return ResponseEntity.ok(shops);
    }

    @PutMapping("/async/{id}")
    @PreAuthorize("@accessControlService.isCompanyWorker(authentication, #id) or @accessControlService.isAdmin(authentication)")
    public ResponseEntity<Void> updateShop(@PathVariable Long id,
                                           @RequestBody ShopRequest shopRequest,
                                           HttpServletRequest httpRequest) {
        log.debug("Received request to update shop {}: {}", id, shopRequest);
        shopService.updateShopAsync(id, shopRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accessControlService.isCompanyWorker(authentication, #id) or @accessControlService.isAdmin(authentication)")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id, HttpServletRequest httpRequest) {
        shopService.deleteShop(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category")
    public ResponseEntity<List<ShopResponse>> getShopsByCategory(
            @RequestParam("category") List<CategoryEnum> category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
        List<ShopResponse> shops = shopService.getShopsByCategory(category, page, size);
        return ResponseEntity.ok(shops);
    }
}
