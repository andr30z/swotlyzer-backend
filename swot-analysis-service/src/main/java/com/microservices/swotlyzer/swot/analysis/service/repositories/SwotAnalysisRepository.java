package com.microservices.swotlyzer.swot.analysis.service.repositories;


import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwotAnalysisRepository extends MongoRepository<SwotAnalysis, String> {
    Page<SwotAnalysis> findByOwnerId(Long ownerId, Pageable pageable);

    /**
    * this naming sucks
    * 
    **/
    Optional<SwotAnalysis> findBy_idAndOwnerId(String swotId, Long ownerId);
}
