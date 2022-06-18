package com.api.swotlyzer.repositories;

import com.api.swotlyzer.models.SwotAnalysis;
import com.api.swotlyzer.models.SwotField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwotFieldRepository extends MongoRepository<SwotField, String> {
   // Page<SwotField> findAnalysisById(SwotAnalysis swotAnalysis, Pageable pageable);
}
