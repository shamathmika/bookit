package com._2.BookIt.Repository;

import com._2.BookIt.Model.BookingStats;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingStatsRepository extends MongoRepository<BookingStats, ObjectId> {
    List<BookingStats> findByRestaurantID(ObjectId restaurantId);
}

