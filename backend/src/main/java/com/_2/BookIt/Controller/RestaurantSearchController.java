package com._2.BookIt.Controller;
import com._2.BookIt.Dto.RestaurantDetailsResponse;
import com._2.BookIt.Dto.RestaurantSearchResponse;
import com._2.BookIt.Service.RestaurantSearchService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantSearchController {

    @Autowired
    private RestaurantSearchService restaurantSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantSearchResponse>> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "San Jose") String location,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String zipCode,
            @RequestParam(required = false) String datetime,
            @RequestParam(required = false, defaultValue = "1") int people
    ) {
        List<RestaurantSearchResponse> response = restaurantSearchService.search(name, location, state, zipCode, datetime, people);
        return ResponseEntity.ok(response);
    }

    @Parameter(
            description = "MongoDB ObjectId (24-hex characters)",
            example = "5f8f8c44b54764421b7156c7"
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantById(@PathVariable String id) {
        return ResponseEntity.ok(restaurantSearchService.getRestaurantDetails(id));
    }
}

