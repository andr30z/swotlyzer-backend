package com.microservices.swotlyzer.swot.analysis.service.services;


import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.PaginationResponse;
import com.microservices.swotlyzer.swot.analysis.service.dtos.SuccessDeleteSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.UpdateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;

public interface SwotAnalysisService {

    SwotAnalysis findById(String id);

    PaginationResponse<SwotAnalysis> findByCurrentUser(int page, int perPage);

    SwotAnalysis create(CreateSwotAnalysisDTO createSWOTAnalysisDTO);

    SwotAnalysis update(String swotAnalysisId, UpdateSwotAnalysisDTO updateSWOTAnalysisDTO);

    SwotAnalysis getSwotAnalysisByCurrentUser(String swotAnalysisId);

    SuccessDeleteSwotAnalysisDTO deleteSwotAnalysisByCurrentUser(String swotAnalysisId);
}
