package com._2.BookIt.Service;

// Project packages

import com._2.BookIt.Enum.TableStatus;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.TableRepository;

// BSON packages
import org.bson.types.ObjectId;

// Spring packages
import org.springframework.stereotype.Service;

// Java packages
import java.util.List;

@Service
public class TableService {
	private TableRepository tableRepository;
	
	public List<Table> findAvailableTables (ObjectId restaurantID) {
		return tableRepository.findByRestaurantIDAndStatus(restaurantID, TableStatus.AVAILABLE);
	}
}
