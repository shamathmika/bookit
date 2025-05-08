package com._2.BookIt.Controller;

import com._2.BookIt.Dto.ReviewResponse;
import com._2.BookIt.Dto.StandaloneReviewRequest;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Model.Review;
import com._2.BookIt.Repository.RestaurantRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping ("/api/reviews")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@PostMapping
	@PreAuthorize ("hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Review> createReview (
			@RequestPart ("request") @Valid Review review,
			@RequestPart (value = "photos", required = false) List<MultipartFile> photos) {
		return ResponseEntity.ok(reviewService.createReview(review, photos));
	}
	
	@GetMapping ("/restaurant/{restaurantId}")
	public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurant (@PathVariable String restaurantId) {
		List<ReviewResponse> response = reviewService.getReviewsByRestaurant(new ObjectId(restaurantId));
		return ResponseEntity.ok(response);
	}
	
	@GetMapping ("/user/{customerID}")
	@PreAuthorize ("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Review>> getByCustomer (@PathVariable String customerID) {
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
	
	
	@GetMapping ("/admin")
	@PreAuthorize ("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Review>> getAll () {
		return ResponseEntity.ok(reviewService.getAllReviews());
	}
	
	@DeleteMapping ("/{id}")
	@PreAuthorize ("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteReview (@PathVariable String id) {
		reviewService.deleteReview(new ObjectId(id));
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping ("/user/{customerId}/review/{reviewId}")
	@PreAuthorize ("hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Review> updateReview (
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
	
	
	@GetMapping ("/debug/all-reviews")
	public List<Review> getAllReviews () {
		List<Review> reviews = reviewRepository.findAll();
		System.out.println("📦 Total reviews in repo: " + reviews.size());
		reviews.forEach(r -> System.out.println("📄 Review: " + r));
		return reviews;
	}

	@PostMapping("/standalone")
	@PreAuthorize("hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Review> postStandaloneReview(
			@RequestPart("request") @Valid StandaloneReviewRequest request,
			@RequestPart(value = "photos", required = false) List<MultipartFile> photos) {

		Review review = Review.builder()
				.restaurantID(new ObjectId(request.getRestaurantID()))
				.customerID(new ObjectId(request.getCustomerID()))
				.rating(request.getRating())
				.comments(request.getComments())
				.date(request.getDate() != null ? request.getDate() : new Date())
				.build();

		// Upload photos if present
		List<MultipartFile> photoList = photos != null ? photos : List.of();
		List<String> imageUrls = reviewService.uploadReviewImages(photoList);
		review.setPhotos(imageUrls);

		// Save review
		Review saved = reviewRepository.save(review);

		// ⬇️ Recalculate and update avg star rating
		ObjectId restaurantId = saved.getRestaurantID();
		List<Review> allReviews = reviewRepository.findByRestaurantID(restaurantId);
		double avgRating = allReviews.stream()
				.mapToInt(Review::getRating)
				.average()
				.orElse(0.0);

		// Optional: round to 1 decimal
		avgRating = Math.round(avgRating * 10) / 10.0;

		// Update restaurant
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
		restaurant.setAvgStarRating(avgRating);
		restaurantRepository.save(restaurant);
		System.out.println(">>> Saved avgRating: " + restaurant.getAvgStarRating());
		System.out.println(">>> Reloaded from DB: " + restaurantRepository.findById(restaurant.getId()).get().getAvgStarRating());
		return ResponseEntity.ok(saved);
	}
	
}
