package com.microservices.swotlyzer.api.core.repositories;

import com.microservices.swotlyzer.api.core.models.SwotField;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwotFieldRepository extends MongoRepository<SwotField, String> {
   // Page<SwotField> findAnalysisById(SwotAnalysis swotAnalysis, Pageable pageable);
}
