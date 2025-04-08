package com._2.BookIt.Service;

// Project packages

import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Repository.RestaurantRepository;

// Spring packages
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
	private RestaurantRepository restaurantRepository;
	
	public Restaurant getRestaurantById (String id) {
		return restaurantRepository.findById(id).orElse(null);
	}
}
