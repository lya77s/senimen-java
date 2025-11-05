package com.senimen.repositories;

import com.senimen.models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByLocation_CityIgnoreCase(String city);
    List<Event> findByDateBetween(Instant from, Instant to);
    List<Event> findByTagsIn(List<String> tags);
}
