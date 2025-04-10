package com._2.BookIt.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com._2.BookIt.Util.ObjectIdSerializer; // make sure the import is correct

@Document(collection = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string", description = "MongoDB ObjectId")
    private ObjectId id;

    @NotNull(message = "restaurantID is required")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string", description = "MongoDB ObjectId")
    private ObjectId restaurantID;

    @NotNull(message = "customerID is required")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string", description = "MongoDB ObjectId")
    private ObjectId customerID;

    @NotNull(message = "bookingID is required")
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId bookingID;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @Size(max = 1000, message = "Comments cannot be longer than 1000 characters")
    private String comments;

    private List<@NotBlank(message = "Photo URL cannot be blank") String> photos;

    @PastOrPresent(message = "Date must be in the past or present")
    @Builder.Default
    private Date date = new Date();
}
