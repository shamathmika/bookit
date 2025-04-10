package com._2.BookIt.Service;

import com._2.BookIt.Model.BookingStats;
import com._2.BookIt.Repository.BookingStatsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingStatsService {

    @Autowired
    private BookingStatsRepository repository;

    public BookingStats create(BookingStats stats) {
        return repository.save(stats);
    }

    public List<BookingStats> getAll() {
        return repository.findAll();
    }

    public List<BookingStats> getByRestaurant(ObjectId restaurantId) {
        return repository.findByRestaurantID(restaurantId);
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }
}

