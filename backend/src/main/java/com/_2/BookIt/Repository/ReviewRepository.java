package com._2.BookIt.Repository;

import com._2.BookIt.Dto.ReviewCount;
import com._2.BookIt.Model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByRestaurantID(ObjectId restaurantId);
    List<Review> findByCustomerID(ObjectId customerID);
    @Aggregation(pipeline = {
            "{ '$match': { 'restaurantID': { '$in': ?0 } } }",
            "{ '$group': { '_id': '$restaurantID', 'count': { '$sum': 1 } } }"
    })
    List<ReviewCount> countReviewsByRestaurant(List<ObjectId> restaurantIds);
}

