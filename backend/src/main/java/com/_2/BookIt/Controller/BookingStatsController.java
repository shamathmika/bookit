package com._2.BookIt.Controller;

import com._2.BookIt.Model.BookingStats;
import com._2.BookIt.Service.BookingStatsService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-stats")
public class BookingStatsController {

    @Autowired
    private BookingStatsService service;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<BookingStats> create(@Valid @RequestBody BookingStats stats) {
        return ResponseEntity.ok(service.create(stats));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<BookingStats>> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(service.getByRestaurant(new ObjectId(restaurantId)));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingStats>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }
}
