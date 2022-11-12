package com.microservices.swotlyzer.swot.analysis.service.services.impl;

import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.PaginationResponse;
import com.microservices.swotlyzer.swot.analysis.service.dtos.SuccessDeleteSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.UpdateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotArea;
import com.microservices.swotlyzer.swot.analysis.service.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.swot.analysis.service.services.SwotAnalysisService;
import com.microservices.swotlyzer.swot.analysis.service.utils.PaginationUtil;
import com.microservices.swotlyzer.common.config.dtos.UserHeaderInfo;
import com.microservices.swotlyzer.common.config.utils.WebClientUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.error.handling.OperationNotAllowedException;
import web.error.handling.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Objects;

@Service
public class SwotAnalysisServiceImpl implements SwotAnalysisService {
    private final SwotAnalysisRepository swotAnalysisRepository;
    private final HttpServletRequest httpServletRequest;

    public SwotAnalysisServiceImpl(SwotAnalysisRepository swotAnalysisRepository,
            HttpServletRequest httpServletRequest) {
        this.swotAnalysisRepository = swotAnalysisRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public SwotAnalysis findById(String id) {
        return this.swotAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SWOT Analysis not found."));
    }

    @Override
    public PaginationResponse<SwotAnalysis> findByCurrentUser(int page, int perPage) {
        Pageable paging = PageRequest.of(page - 1, perPage);
        UserHeaderInfo userHeaderInfo = WebClientUtils.getUserHeadersInfo(httpServletRequest);
        Page<SwotAnalysis> swotPage = this.swotAnalysisRepository.findByOwnerId(userHeaderInfo.getUserId(), paging);
        return new PaginationUtil<SwotAnalysis, SwotAnalysisRepository>().buildResponse(swotPage);
    }

    @Override
    public SwotAnalysis create(CreateSwotAnalysisDTO createSWOTAnalysisDTO) {

        SwotAnalysis swotAnalysis = new SwotAnalysis();
        var swotStrengthsArea = new SwotArea("Strengths", "green", Collections.emptySet());
        var swotOpportunitiesArea = new SwotArea("Opportunities", "blue", Collections.emptySet());
        var swotWeaknessesArea = new SwotArea("Weaknesses", "red", Collections.emptySet());
        var swotThreatsArea = new SwotArea("Threats", "grey", Collections.emptySet());

        swotAnalysis.setWeaknesses(swotWeaknessesArea);
        swotAnalysis.setStrengths(swotStrengthsArea);
        swotAnalysis.setThreats(swotThreatsArea);
        swotAnalysis.setOpportunities(swotOpportunitiesArea);
        BeanUtils.copyProperties(createSWOTAnalysisDTO, swotAnalysis);

        UserHeaderInfo userHeaderInfo = WebClientUtils.getUserHeadersInfo(httpServletRequest);
        Long currentUserId = userHeaderInfo.getUserId();
        swotAnalysis.setOwnerId(currentUserId);
        swotAnalysis.setSwotLayoutType(createSWOTAnalysisDTO.getLayoutType());
        return this.swotAnalysisRepository.save(swotAnalysis);
    }

    @Override
    public SwotAnalysis getSwotAnalysisByCurrentUser(String swotAnalysisId) {
        SwotAnalysis swotAnalysis = this.swotAnalysisRepository.findById(swotAnalysisId)
                .orElseThrow(() -> new ResourceNotFoundException("SWOT Analysis don't exist."));
        var userHeaderInfo = WebClientUtils.getUserHeadersInfo(httpServletRequest);
        Long currentUserId = userHeaderInfo.getUserId();
        if (!Objects.equals(swotAnalysis.getOwnerId(), currentUserId))
            throw new OperationNotAllowedException(
                    "SWOT Analysis with _id: " + swotAnalysisId + " doesn't belongs to user with " + "id: " +
                            currentUserId);
        return swotAnalysis;
    }

    @Override
    public SuccessDeleteSwotAnalysisDTO deleteSwotAnalysisByCurrentUser(String swotAnalysisId) {
        var swotAnalysis = getSwotAnalysisByCurrentUser(swotAnalysisId);
        swotAnalysisRepository.delete(swotAnalysis);
        return new SuccessDeleteSwotAnalysisDTO("Swot analysis deleted successfully.");
    }

    @Override
    public SwotAnalysis update(String swotAnalysisId, UpdateSwotAnalysisDTO updateSWOTAnalysisDTO) {

        var swotAnalysis = getSwotAnalysisByCurrentUser(swotAnalysisId);
        BeanUtils.copyProperties(updateSWOTAnalysisDTO, swotAnalysis);
        swotAnalysis.setSwotLayoutType(updateSWOTAnalysisDTO.getLayoutType());

        return this.swotAnalysisRepository.save(swotAnalysis);
    }
}
