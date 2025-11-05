package com.senimen.repositories;

import com.senimen.models.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByEventId(String eventId);
    List<Application> findByVolunteerId(String volunteerId);
}
