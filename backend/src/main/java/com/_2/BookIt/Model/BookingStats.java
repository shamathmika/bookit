package com._2.BookIt.Model;

import com._2.BookIt.Util.ObjectIdDeserializer;
import com._2.BookIt.Util.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookingStats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStats {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string", description = "MongoDB ObjectId")
    private ObjectId id;

    @NotNull(message = "restaurantID is required")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Schema(type = "string")
    private ObjectId restaurantID;

    @NotNull(message = "Month is required")
    private String month;

    @Builder.Default
    private int totalBookings = 0;

    @Builder.Default
    private int totalCancellations = 0;
}