package com._2.BookIt.Repository;

import com._2.BookIt.Model.Booking;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, ObjectId> {
    List<Booking> findByUserID(ObjectId userID);
    List<Booking> findByRestaurantID(ObjectId restaurantID);
}
