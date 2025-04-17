package com._2.BookIt.Controller;

import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.BookingStats;
import com._2.BookIt.Repository.RestaurantRepository;
import com._2.BookIt.Service.BookingService;
import com._2.BookIt.Service.BookingStatsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/booking-stats")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookingStatsController {

    private final BookingService bookingService;
    private final BookingStatsService bookingStatsService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/analytics/summary")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAnalyticsSummary() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalReservations", bookingService.getTotalBookingCount());
        data.put("restaurantCount", restaurantRepository.count());
        data.put("pendingApprovals", restaurantRepository.countByStatus("pending"));
        return ResponseEntity.ok(data);
    }

    @GetMapping("/analytics/monthly")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyBookings() {
        List<Map<String, Object>> response = bookingStatsService.getMonthlyAggregates();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/analytics/popular-slots")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Long>> getPopularTimeSlots() {
        List<Booking> bookings = bookingService.getAllBookings();
        Map<String, Long> freq = bookings.stream()
                .map(b -> {
                    return b.getDateTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .getHour() + ":00";
                })

                .collect(Collectors.groupingBy(slot -> slot, TreeMap::new, Collectors.counting()));
        return ResponseEntity.ok(freq);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingStats>> getAllStats() {
        return ResponseEntity.ok(bookingStatsService.getAllStats());
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<BookingStats> updateStats(@RequestParam String restaurantId,
                                                    @RequestParam String dateTime,
                                                    @RequestParam boolean isConfirmed) {
        return ResponseEntity.ok(bookingStatsService.createOrUpdateStats(
                new org.bson.types.ObjectId(restaurantId),
                java.time.LocalDateTime.parse(dateTime),
                isConfirmed));
    }
}