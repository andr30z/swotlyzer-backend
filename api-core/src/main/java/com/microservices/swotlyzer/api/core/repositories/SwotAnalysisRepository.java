package com.microservices.swotlyzer.api.core.repositories;


import com.microservices.swotlyzer.api.core.models.User;
import com.microservices.swotlyzer.api.core.models.SwotAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwotAnalysisRepository extends MongoRepository<SwotAnalysis, String> {
    Page<SwotAnalysis> findByCreator(User user, Pageable pageable);
}
