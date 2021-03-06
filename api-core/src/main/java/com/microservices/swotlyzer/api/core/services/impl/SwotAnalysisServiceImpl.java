package com.microservices.swotlyzer.api.core.services.impl;

import com.microservices.swotlyzer.api.core.dtos.*;
import com.microservices.swotlyzer.api.core.utils.*;
import com.microservices.swotlyzer.api.core.dtos.UpdateSwotAnalysisDTO;
import com.microservices.swotlyzer.api.core.models.SwotAnalysis;
import com.microservices.swotlyzer.api.core.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.api.core.services.SwotAnalysisService;
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

        var userHeaderInfo = WebClientUtils.getUserHeadersInfo(httpServletRequest);
        Page<SwotAnalysis> swotPage = this.swotAnalysisRepository.findByOwnerId(userHeaderInfo.getUserId(), paging);
        return new PaginationUtil<SwotAnalysis, SwotAnalysisRepository>().buildResponse(swotPage);
    }

    @Override
    public SwotAnalysis create(CreateSwotAnalysisDTO createSWOTAnalysisDTO) {

        SwotAnalysis swotAnalysis = new SwotAnalysis();
        swotAnalysis.setWeaknesses(Collections.emptyList());
        swotAnalysis.setStrengths(Collections.emptyList());
        swotAnalysis.setThreats(Collections.emptyList());
        swotAnalysis.setOpportunities(Collections.emptyList());
        BeanUtils.copyProperties(createSWOTAnalysisDTO, swotAnalysis);

        var userHeaderInfo = WebClientUtils.getUserHeadersInfo(httpServletRequest);
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
        if (!Objects.equals(swotAnalysis.getOwnerId(), currentUserId)) throw new OperationNotAllowedException(
                "SWOT Analysis with _id: " + swotAnalysisId + " don't belongs to user with " + "id: " +
                        currentUserId);
        return swotAnalysis;
    }

    @Override
    public SwotAnalysis update(String swotAnalysisId, UpdateSwotAnalysisDTO updateSWOTAnalysisDTO) {

        var swotAnalysis = getSwotAnalysisByCurrentUser(swotAnalysisId);
        BeanUtils.copyProperties(updateSWOTAnalysisDTO, swotAnalysis);
        swotAnalysis.setSwotLayoutType(updateSWOTAnalysisDTO.getLayoutType());


        return this.swotAnalysisRepository.save(swotAnalysis);
    }
}
