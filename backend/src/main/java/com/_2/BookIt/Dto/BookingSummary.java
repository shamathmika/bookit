package com._2.BookIt.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSummary {
    private String restaurantName;
    private int totalBookings;
    private int totalCancellations;
    private String successRate;
}

