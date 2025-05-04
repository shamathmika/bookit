package com._2.BookIt.Service;

import com._2.BookIt.Enum.ApprovalStatus;
import com._2.BookIt.Enum.RestaurantStatus;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.RestaurantRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class AdminRestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Restaurant approveRestaurant(String id) {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setApprovalStatus(ApprovalStatus.APPROVED);
        restaurant.setStatus(RestaurantStatus.ACTIVE);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant rejectRestaurant(String id) {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setApprovalStatus(ApprovalStatus.REJECTED);
        restaurant.setStatus(RestaurantStatus.INACTIVE);
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(new ObjectId(id));
    }

    public List<Restaurant> getPendingRestaurants() {
        return restaurantRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }

    public Map<String, Object> getAdminDashboardStats() {
        long totalRestaurants = restaurantRepository.count();
        long pendingApprovals = restaurantRepository.countByApprovalStatus(ApprovalStatus.PENDING);
        long totalBookingsLastMonth = Optional.ofNullable(bookingRepository.countBookingsLastMonth()).orElse(0L);

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalRestaurants", totalRestaurants);
        dashboard.put("pendingApprovals", pendingApprovals);
        dashboard.put("totalBookingsLastMonth", totalBookingsLastMonth);

        return dashboard;
    }

    private Restaurant findRestaurantById(String id) {
        return restaurantRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }
}

