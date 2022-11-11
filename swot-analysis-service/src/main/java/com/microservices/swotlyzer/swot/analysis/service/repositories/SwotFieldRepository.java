package com.microservices.swotlyzer.swot.analysis.service.repositories;

import com.microservices.swotlyzer.swot.analysis.service.models.SwotField;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwotFieldRepository extends MongoRepository<SwotField, String> {
   // Page<SwotField> findAnalysisById(SwotAnalysis swotAnalysis, Pageable pageable);
}
