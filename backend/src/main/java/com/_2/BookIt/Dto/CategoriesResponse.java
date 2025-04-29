package com._2.BookIt.Dto;

// Project packages

import com._2.BookIt.Model.Restaurant;

// Lombok packages
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Java packages
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesResponse {
	private List<Restaurant> topRated;
	private List<Restaurant> topBookedToday;
	private List<Restaurant> nearYou;
}
