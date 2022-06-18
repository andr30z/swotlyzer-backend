package com.api.swotlyzer.repositories;

import com.api.swotlyzer.models.SwotAnalysis;
import com.api.swotlyzer.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwotAnalysisRepository extends MongoRepository<SwotAnalysis, String> {
    Page<SwotAnalysis> findByCreator(User user, Pageable pageable);
}
