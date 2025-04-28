package com._2.BookIt.Controller;

import com._2.BookIt.Model.Review;
import com._2.BookIt.Repository.ReviewRepository;
import com._2.BookIt.Security.SecurityUtil;
import com._2.BookIt.Service.ReviewService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<Review>> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(reviewService.getReviewsByRestaurant(new ObjectId(restaurantId)));
    }

    @GetMapping("/user/{customerID}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Review>> getByCustomer(@PathVariable String customerID) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        boolean isAdmin = SecurityUtil.hasRole("ADMIN");

        // Customers can only access their own reviews
        if (!isAdmin && !currentUserId.equals(customerID)) {
            throw new AccessDeniedException("You are not authorized to view reviews of other users.");
        }

        ObjectId objId = new ObjectId(customerID);
        List<Review> results = reviewService.getReviewsByCustomer(objId);
        return ResponseEntity.ok(results);
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Review>> getAll() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user/{customerId}/review/{reviewId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Review> updateReview(
            @PathVariable String customerId,
            @PathVariable String reviewId,
            @RequestBody Review updatedReview) {

        Review result = reviewService.updateReviewByCustomer(
                new ObjectId(customerId),
                new ObjectId(reviewId),
                updatedReview
        );
        return ResponseEntity.ok(result);
    }


    @GetMapping("/debug/all-reviews")
    public List<Review> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        System.out.println("📦 Total reviews in repo: " + reviews.size());
        reviews.forEach(r -> System.out.println("📄 Review: " + r));
        return reviews;
    }
}
