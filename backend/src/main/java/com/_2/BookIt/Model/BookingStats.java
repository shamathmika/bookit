package com._2.BookIt.Model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

@Document(collection = "bookingStats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStats {

    @Id
    private ObjectId id;

    @NotNull(message = "restaurantID is required")
    private ObjectId restaurantID;

    @NotBlank(message = "Month is required")
    private String month; // Format suggestion: "2025-04" or "April 2025"

    @Builder.Default
    @Min(value = 0, message = "Total bookings cannot be negative")
    private Integer totalBookings = 0;

    @Builder.Default
    @Min(value = 0, message = "Total cancellations cannot be negative")
    private Integer totalCancellations = 0;
}
