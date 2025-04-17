package com._2.BookIt.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

// --- DTO ---
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableRestaurantResponse {
    private String restaurantName;
    private String cuisine;
    private int costRating;
    private double avgRating;
    private long totalReviews;
    private long bookedToday;
    private List<String> availableTimes;
}