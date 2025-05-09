package com._2.BookIt.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyAggregate {
    private int year;
    private int month;
    private long totalBookings;
}