package com._2.BookIt.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSearchResponse {
    private String restaurantId;      // ← new
    private String restaurantName;
    private String cuisine;
    private int costRating;
    private double avgRating;
    private List<String> photos;
    private long totalReviews;
    private long bookedToday;
    private List<String> availableTimes;
}
