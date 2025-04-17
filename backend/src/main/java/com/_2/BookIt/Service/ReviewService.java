package com._2.BookIt.Service;

import com._2.BookIt.Model.Review;
import com._2.BookIt.Repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByCustomer(ObjectId customerID) {
        return reviewRepository.findByCustomerID(customerID);
    }

    public List<Review> getReviewsByRestaurant(ObjectId restaurantId) {
        return reviewRepository.findByRestaurantID(restaurantId);
    }

    public void deleteReview(ObjectId id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReviewByCustomer(ObjectId customerId, ObjectId reviewId, Review updatedReview) {
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
        existingReview.setPhotos(updatedReview.getPhotos());
        existingReview.setDate(new Date()); // update timestamp

        return reviewRepository.save(existingReview);

    }
    public List<Review> getReviewsByUserId(String userId) {
        ObjectId objectId = new ObjectId(userId);
        return reviewRepository.findByCustomerID(objectId);
    }


}
