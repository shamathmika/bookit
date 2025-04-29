package com._2.BookIt.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StandaloneReviewRequest {

    @NotNull(message = "restaurantID is required")
    private String restaurantID;

    @NotNull(message = "customerID is required")
    private String customerID;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @Size(max = 1000, message = "Comments cannot be longer than 1000 characters")
    private String comments;

    private List<@NotBlank(message = "Photo URL cannot be blank") String> photos;

    private Date date = new Date(); // Defaults to current date if not provided
}
