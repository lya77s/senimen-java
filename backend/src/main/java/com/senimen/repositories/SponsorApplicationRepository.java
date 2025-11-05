package com.senimen.repositories;

import com.senimen.models.SponsorApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SponsorApplicationRepository extends MongoRepository<SponsorApplication, String> {}
