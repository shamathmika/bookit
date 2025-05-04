package com._2.BookIt.Controller;

import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Service.AdminRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminRestaurantController {

    @Autowired
    private AdminRestaurantService adminRestaurantService;

    // Approve a restaurant
    @PutMapping("/{id}/approve")
    public ResponseEntity<Restaurant> approveRestaurant(@PathVariable String id) {
        return ResponseEntity.ok(adminRestaurantService.approveRestaurant(id));
    }

    // Reject a restaurant
    @PutMapping("/{id}/reject")
    public ResponseEntity<Restaurant> rejectRestaurant(@PathVariable String id) {
        return ResponseEntity.ok(adminRestaurantService.rejectRestaurant(id));
    }

    // Delete a restaurant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        adminRestaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // Get all pending approval restaurants
    @GetMapping("/pending")
    public ResponseEntity<List<Restaurant>> getPendingRestaurants() {
        return ResponseEntity.ok(adminRestaurantService.getPendingRestaurants());
    }

    // Admin Dashboard (total restaurants, total pending approvals, total bookings last month)
    @GetMapping("/dashboard")
    public ResponseEntity<?> getAdminDashboard() {
        return ResponseEntity.ok(adminRestaurantService.getAdminDashboardStats());
    }

    // Get all restaurants
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(adminRestaurantService.getAllRestaurants());
    }

}
