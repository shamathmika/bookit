package com._2.BookIt.Model;

import com._2.BookIt.Enum.BookingStatus;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    private ObjectId id;

    @NotNull(message = "restaurantID is required")
    private ObjectId restaurantID;

    @NotNull(message = "tableID is required")
    private ObjectId tableID;

    @NotNull(message = "userID is required")
    private ObjectId userID;

    @NotNull(message = "dateTime is required")
    @FutureOrPresent(message = "Booking date must be in the future or present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    @NotNull(message = "totalCustomers is required")
    @Min(value = 1, message = "At least one customer is required")
    private Integer totalCustomers;

    @NotNull(message = "status is required")
    private BookingStatus status;
}

