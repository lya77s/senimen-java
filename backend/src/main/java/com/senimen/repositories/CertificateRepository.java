package com.senimen.repositories;

import com.senimen.models.Certificate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CertificateRepository extends MongoRepository<Certificate, String> {
    List<Certificate> findByVolunteerId(String volunteerId);
}
