package com._2.BookIt.Repository;

import com._2.BookIt.Model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByRestaurantID(ObjectId restaurantId);
    List<Review> findByCustomerID(ObjectId customerID);
}

