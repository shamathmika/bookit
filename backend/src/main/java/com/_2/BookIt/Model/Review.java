package com._2.BookIt.Model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    private ObjectId id;

    @NotNull(message = "restaurantID is required")
    private ObjectId restaurantID;

    @NotNull(message = "customerID is required")
    private ObjectId customerID;

    @NotNull(message = "bookingID is required")
    private ObjectId bookingID;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "1.0", message = "Rating must be at least 1")
    @DecimalMax(value = "5.0", message = "Rating must not exceed 5")
    private Float rating;

    @Size(max = 1000, message = "Comments cannot be longer than 1000 characters")
    private String comments;

    // Optional list, but if provided, entries must be valid
    private List<@NotBlank(message = "Photo URL cannot be blank") String> photos;

    @Builder.Default
    @PastOrPresent(message = "Date must be in the past or present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date = LocalDateTime.now();
}
