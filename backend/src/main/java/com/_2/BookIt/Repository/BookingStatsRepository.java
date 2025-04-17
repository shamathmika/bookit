package com._2.BookIt.Repository;

import com._2.BookIt.Model.BookingStats;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookingStatsRepository extends MongoRepository<BookingStats, ObjectId> {
    Optional<BookingStats> findByRestaurantIDAndMonth(ObjectId restaurantID, String month);

    @Aggregation(pipeline = {
            "{ $group: { _id: '$month', totalBookings: { $sum: '$totalBookings' }, totalCancellations: { $sum: '$totalCancellations' } } }",
            "{ $sort: { _id: 1 } }"
    })
    List<Map<String, Object>> aggregateByMonth();
}