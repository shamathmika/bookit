package com._2.BookIt.Service;

import com._2.BookIt.Dto.ReviewResponse;
import com._2.BookIt.Model.Review;
import com._2.BookIt.Model.User;
import com._2.BookIt.Repository.ReviewRepository;
import com._2.BookIt.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private S3Service s3Service;
	
	public Review createReview (Review review, List<MultipartFile> images) {
		if (images != null && !images.isEmpty()) {
			List<String> imageUrls = s3Service.uploadImages("reviews", images);
			review.setPhotos(imageUrls);
		}
		return reviewRepository.save(review);
	}
	
	public List<String> uploadReviewImages (List<MultipartFile> photos) {
		if (photos == null || photos.isEmpty()) return List.of();
		return s3Service.uploadImages("reviews", photos);
	}
	
	public List<Review> getAllReviews () {
		return reviewRepository.findAll();
	}
	
	public List<Review> getReviewsByCustomer (ObjectId customerID) {
		return reviewRepository.findByCustomerID(customerID);
	}
	
	public List<ReviewResponse> getReviewsByRestaurant (ObjectId restaurantId) {
		List<Review> reviews = reviewRepository.findByRestaurantID(restaurantId);
		List<ReviewResponse> result = new ArrayList<>();
		
		for (Review review : reviews) {
			String customerName = userRepository.findById(review.getCustomerID().toHexString())
					.map(User::getName)
					.orElse("Unknown");
			result.add(new ReviewResponse(review, customerName));
		}
		
		return result;
	}
	
	public void deleteReview (ObjectId id) {
		Review review = reviewRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
		
		List<String> photos = review.getPhotos();
		if (photos != null) {
			for (String photoUrl : photos) {
				s3Service.deleteImage(photoUrl);
			}
		}
		
		reviewRepository.deleteById(id);
	}
	
	public Review updateReviewByCustomer (ObjectId customerId, ObjectId reviewId, Review updatedReview) {
		Optional<Review> existingReviewOpt = reviewRepository.findById(reviewId);
		
		if (existingReviewOpt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
		}
		
		Review existingReview = existingReviewOpt.get();
		
		if (!existingReview.getCustomerID().equals(customerId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own reviews");
		}
		
		// Update allowed fields only
		existingReview.setRating(updatedReview.getRating());
		existingReview.setComments(updatedReview.getComments());
		existingReview.setPhotos(updatedReview.getPhotos()); // TODO: Delete/add actual photos
		existingReview.setDate(new Date()); // update timestamp
		
		return reviewRepository.save(existingReview);
		
	}
	
	public List<Review> getReviewsByUserId (String userId) {
		ObjectId objectId = new ObjectId(userId);
		return reviewRepository.findByCustomerID(objectId);
	}
	
	
}
