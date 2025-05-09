package com._2.BookIt.Service;

import com._2.BookIt.Dto.BookingSummary;
import com._2.BookIt.Model.BookingStats;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.BookingStatsRepository;
import com._2.BookIt.Repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingStatsService {

    private final BookingStatsRepository statsRepo;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public BookingStats createOrUpdateStats(ObjectId restaurantId, LocalDateTime dateTime, boolean isConfirmed) {
        String monthKey = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        BookingStats stats = statsRepo.findByRestaurantIDAndMonth(restaurantId, monthKey)
                .orElse(BookingStats.builder()
                        .restaurantID(restaurantId)
                        .month(monthKey)
                        .totalBookings(0)
                        .totalCancellations(0)
                        .build());

        if (isConfirmed) {
            stats.setTotalBookings(stats.getTotalBookings() + 1);
        } else {
            stats.setTotalCancellations(stats.getTotalCancellations() + 1);
        }

        return statsRepo.save(stats);
    }

    public List<BookingSummary> getLiveBookingStats() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<BookingSummary> summaries = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            ObjectId restId = restaurant.getId();
            String name = restaurant.getName();

            long total = bookingRepository.countByRestaurantID(restId);
            long cancelled = bookingRepository.countByRestaurantIDAndStatus(restId, "cancelled");

            String successRate;
            if (total == 0) {
                successRate = "N/A";
            } else {
                double rate = ((double)(total - cancelled) / total) * 100;
                successRate = Math.round(rate) + "%";
            }

            BookingSummary summary = new BookingSummary(name, (int) total, (int) cancelled, successRate);
            summaries.add(summary);
        }

        return summaries;
    }



    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Map<String, Object>> getMonthlyAggregates() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project()
                        .andExpression("year(dateTime)").as("year")
                        .andExpression("month(dateTime)").as("month"),
                Aggregation.group("year", "month").count().as("totalBookings"),
                Aggregation.sort(Sort.Direction.ASC, "_id.year", "_id.month"),
                Aggregation.project("totalBookings")
                        .and("_id.year").as("year")
                        .and("_id.month").as("month")
        );

        AggregationResults<Map> results =
                mongoTemplate.aggregate(aggregation, "booking", Map.class);

        return (List<Map<String, Object>>) (List<?>) results.getMappedResults();
    }



}