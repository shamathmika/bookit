package com._2.BookIt.Repository;

import com._2.BookIt.Dto.BookingCount;
import com._2.BookIt.Model.Booking;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, ObjectId> {
    List<Booking> findByUserID(ObjectId userID);
    List<Booking> findByRestaurantID(ObjectId restaurantID);
    List<Booking> findByRestaurantIDInAndDateTimeInAndStatusIn(
            List<ObjectId> restaurantIds, List<Date> times, List<String> status
    );


    @Aggregation(pipeline = {
            "{ '$match': { 'status': 'confirmed', 'dateTime': { '$gte': ?1, '$lte': ?2 }, 'restaurantID': { '$in': ?0 } } }",
            "{ '$group': { '_id': '$restaurantID', 'count': { '$sum': 1 } } }"
    })
    List<BookingCount> countConfirmedTodayByRestaurant(List<ObjectId> restaurantIds, Date start, Date end);
}
