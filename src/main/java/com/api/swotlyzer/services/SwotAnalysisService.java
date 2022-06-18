package com.api.swotlyzer.services;

import com.api.swotlyzer.dtos.CreateSwotAnalysisDTO;
import com.api.swotlyzer.dtos.PaginationResponse;
import com.api.swotlyzer.dtos.UpdateSwotAnalysisDTO;
import com.api.swotlyzer.models.SwotAnalysis;

public interface SwotAnalysisService {

    SwotAnalysis findById(String id);

    PaginationResponse<SwotAnalysis> findByCurrentUser(int page, int perPage);

    SwotAnalysis create(CreateSwotAnalysisDTO createSWOTAnalysisDTO);

    SwotAnalysis update(String swotAnalysisId, UpdateSwotAnalysisDTO updateSWOTAnalysisDTO);

}
