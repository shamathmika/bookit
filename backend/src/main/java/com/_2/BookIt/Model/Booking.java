package com._2.BookIt.Model;

import com._2.BookIt.Util.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

import java.util.Date;

@Document(collection = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string", description = "MongoDB ObjectId")
    private ObjectId id;

    @NotNull(message = "restaurantID is required")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string")
    private ObjectId restaurantID;

    @NotNull(message = "tableID is required")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string")
    private ObjectId tableID;

    @NotNull(message = "userID is required")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string")
    private ObjectId userID;

    @NotNull(message = "Booking date/time is required")
    @FutureOrPresent(message = "Booking date must be in the present or future")
    private Date dateTime;

    @Min(value = 1, message = "Total customers must be at least 1")
    private int totalCustomers;

    @Pattern(regexp = "confirmed|pending|cancelled", message = "Status must be 'confirmed', 'pending', or 'cancelled'")
    private String status;
}
