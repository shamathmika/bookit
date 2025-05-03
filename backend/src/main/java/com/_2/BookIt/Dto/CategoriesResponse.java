package com._2.BookIt.Dto;

// Project packages

import com._2.BookIt.Model.Restaurant;

// Lombok packages
import lombok.*;

// Java packages
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesResponse {
	private List<Restaurant> topRated;
	private List<Restaurant> topBookedToday;
	private List<Restaurant> nearYou;
}
