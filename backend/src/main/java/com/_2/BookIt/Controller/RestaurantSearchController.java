package com._2.BookIt.Controller;
import com._2.BookIt.Dto.RestaurantSearchResponse;
import com._2.BookIt.Service.RestaurantSearchService;
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
            @RequestParam(required = false) String datetime,
            @RequestParam(required = false, defaultValue = "1") int people
    ) {
        List<RestaurantSearchResponse> response = restaurantSearchService.search(name, location, datetime, people);
        return ResponseEntity.ok(response);
    }
}

