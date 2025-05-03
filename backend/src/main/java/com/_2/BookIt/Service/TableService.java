package com._2.BookIt.Service;

// Project packages

import com._2.BookIt.Dto.AddTablesRequest;
import com._2.BookIt.Enum.TableStatus;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.TableRepository;

// BSON packages
import org.bson.types.ObjectId;

// Spring packages
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

// Java packages
import java.util.ArrayList;
import java.util.List;

@Service
public class TableService {
	@Autowired
	private TableRepository tableRepository;
	
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
	public void addTables (String restaurantId, AddTablesRequest request) {
		List<Table> existingTables = tableRepository.findByRestaurantID(new ObjectId(restaurantId));
		
		int maxTableNumber = existingTables.stream()
				.mapToInt(Table::getTableNumber)
				.max()
				.orElse(0);
		
		List<Table> newTables = new ArrayList<>();
		for (int i = 1; i <= request.getNumberOfTables(); i++) {
			Table table = Table.builder()
					.restaurantID(new ObjectId(restaurantId))
					.tableNumber(maxTableNumber + i)
					.capacity(request.getTableSize())
					.status(TableStatus.AVAILABLE)
					.build();
			newTables.add(table);
		}
		
		tableRepository.saveAll(newTables);
	}
	
	public List<Table> getTablesByRestaurantId (String restaurantId) {
		return tableRepository.findByRestaurantID(new ObjectId(restaurantId));
	}
	
	public Table updateTableCapacity (String tableId, int newCapacity) {
		Table table = tableRepository.findById(tableId)
				.orElseThrow(() -> new RuntimeException("Table not found"));
		table.setCapacity(newCapacity);
		return tableRepository.save(table);
	}
	
	public void deleteTablesByRestaurantId (String restaurantId) {
		tableRepository.deleteByRestaurantID(new ObjectId(restaurantId));
	}
	
	public void deleteMultipleTablesByRestaurantId (String restaurantId, List<String> tableIds) {
		
		List<Table> tables = tableRepository.findAllById(tableIds);
		
		for (Table table : tables) {
			if (!table.getRestaurantID().toHexString().equals(restaurantId)) {
				throw new RuntimeException("One or more tables do not belong to the specified restaurant.");
			}
		}
		
		tableRepository.deleteAllById(tableIds);
	}
}
