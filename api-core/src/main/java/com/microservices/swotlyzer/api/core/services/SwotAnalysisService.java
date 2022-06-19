package com.microservices.swotlyzer.api.core.services;


import com.microservices.swotlyzer.api.core.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.api.core.dtos.PaginationResponse;
import com.microservices.swotlyzer.api.core.dtos.UpdateSwotAnalysisDTO;
import com.microservices.swotlyzer.api.core.models.SwotAnalysis;

public interface SwotAnalysisService {

    SwotAnalysis findById(String id);

    PaginationResponse<SwotAnalysis> findByCurrentUser(int page, int perPage);

    SwotAnalysis create(CreateSwotAnalysisDTO createSWOTAnalysisDTO);

    SwotAnalysis update(String swotAnalysisId, UpdateSwotAnalysisDTO updateSWOTAnalysisDTO);

}
