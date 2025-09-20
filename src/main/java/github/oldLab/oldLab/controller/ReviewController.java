package github.oldLab.oldLab.controller;

import github.oldLab.oldLab.dto.request.ReviewRequest;
import github.oldLab.oldLab.dto.response.ReviewResponse;
import github.oldLab.oldLab.service.ReviewService;
import github.oldLab.oldLab.serviceImpl.RateLimiterServiceImpl;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final RateLimiterServiceImpl rateLimiterService;

    @PostMapping("/person")
<<<<<<< Updated upstream
    public ResponseEntity<Void> createReviewToPerson(@RequestBody ReviewRequest reviewRequest) {
=======
    public ResponseEntity<Void> createReviewToPerson(@RequestBody ReviewRequest reviewRequest,
                                                     HttpServletRequest httpRequest) {
        log.debug("create review to person: {}", reviewRequest);
>>>>>>> Stashed changes
        reviewService.createReviewToPerson(reviewRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shop")
<<<<<<< Updated upstream
    public ResponseEntity<Void> createReviewToShop(@RequestBody ReviewRequest reviewRequest) {
=======
    public ResponseEntity<Void> createReviewToShop(@RequestBody ReviewRequest reviewRequest,
                                                   HttpServletRequest httpRequest) {
        log.debug("create review to shop: {}", reviewRequest);
>>>>>>> Stashed changes
        reviewService.createReviewToShop(reviewRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByShopId(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "0") int page,
<<<<<<< Updated upstream
            @RequestParam(defaultValue = "20") int size) {
=======
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
        log.debug("get reviews by shopId: {} page: {}, size: {}", shopId, page, size);
>>>>>>> Stashed changes
        List<ReviewResponse> reviews = reviewService.getReviewsByShopId(shopId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByPersonId(
            @PathVariable Long personId,
            @RequestParam(defaultValue = "0") int page,
<<<<<<< Updated upstream
            @RequestParam(defaultValue = "20") int size) {
=======
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
        log.debug("get reviews by personId: {} page: {}, size: {}", personId, page, size);
>>>>>>> Stashed changes
        List<ReviewResponse> reviews = reviewService.getReviewsByPersonId(personId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
<<<<<<< Updated upstream
            @RequestParam(defaultValue = "20") int size) {
=======
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
        log.debug("get all reviews page: {}, size: {}", page, size);
>>>>>>> Stashed changes
        List<ReviewResponse> reviews = reviewService.getAllReviewsPaginated(page, size);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}")
<<<<<<< Updated upstream
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
=======
    @PreAuthorize("@accessControlService.isReviewOwner(authentication, #id) or @accessControlService.isAdmin(authentication) or @accessControlService.isModerator(authentication)")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, HttpServletRequest httpRequest) {
        log.debug("delete review id: {}", id);
>>>>>>> Stashed changes
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}