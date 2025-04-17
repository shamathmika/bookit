package com._2.BookIt.Service;

import com._2.BookIt.Model.BookingStats;
import com._2.BookIt.Repository.BookingStatsRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingStatsService {

    private final BookingStatsRepository statsRepo;

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

    public List<BookingStats> getAllStats() {
        return statsRepo.findAll();
    }

    public List<Map<String, Object>> getMonthlyAggregates() {
        return statsRepo.aggregateByMonth();
    }
}