package github.oldLab.oldLab.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import github.oldLab.oldLab.dto.request.ProductRequest;
import github.oldLab.oldLab.dto.response.ProductResponse;
import github.oldLab.oldLab.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("@accessControlService.hasCompany(authentication)")
    public ResponseEntity<ProductResponse> create(@RequestBody @Validated ProductRequest request,
                                                  @RequestHeader("Authorization") String header,
                                                  HttpServletRequest httpRequest) {
        log.debug("creating product: {}", request);
        return ResponseEntity.ok(productService.create(request, header));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id, HttpServletRequest httpRequest) {
        log.debug("getting product with id: {}", id);
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size,
                                                      HttpServletRequest httpRequest) {
        log.debug("listing products page: {}, size: {}", page, size);
        return ResponseEntity.ok(productService.list(page, size));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ProductResponse>> listByShop(@PathVariable Long shopId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size,
                                                            HttpServletRequest httpRequest) {
        log.debug("listing products by shopId: {}, page: {}, size: {}", shopId, page, size);
        return ResponseEntity.ok(productService.listByShop(shopId, page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accessControlService.isCompanyWorkerByProduct(authentication, #id) or @accessControlService.isAdmin(authentication)")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @RequestBody ProductRequest request,
                                                  HttpServletRequest httpRequest) {
        log.debug("updating product id: {} with: {}", id, request);
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accessControlService.isCompanyWorkerByProduct(authentication, #id) or @accessControlService.isAdmin(authentication)")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        log.debug("deleting product id: {}", id);
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> search(@RequestParam("q") String query,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                        HttpServletRequest httpRequest) {
        log.debug("searching products q: {}, page: {}, size: {}", query, page, size);
        return ResponseEntity.ok(productService.search(query, page, size));
    }

    @GetMapping("/shop/{shopId}/search")
    public ResponseEntity<List<ProductResponse>> searchByShop(@PathVariable Long shopId,
                                                              @RequestParam("q") String query,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size,
                                                              HttpServletRequest httpRequest) {
        log.debug("searching products by shopId: {}, q: {}, page: {}, size: {}", shopId, query, page, size);
        return ResponseEntity.ok(productService.searchByShop(shopId, query, page, size));
    }
}
