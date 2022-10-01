package com.microservices.swotlyzer.api.core.services.impl;

import com.microservices.swotlyzer.api.core.dtos.CreateSwotFieldDTO;
import com.microservices.swotlyzer.api.core.dtos.SwotFieldDeleteResponse;
import com.microservices.swotlyzer.api.core.dtos.UpdateSwotFieldDTO;
import com.microservices.swotlyzer.api.core.enums.SwotFieldType;
import com.microservices.swotlyzer.api.core.models.SwotAnalysis;
import com.microservices.swotlyzer.api.core.models.SwotField;
import com.microservices.swotlyzer.api.core.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.api.core.repositories.SwotFieldRepository;
import com.microservices.swotlyzer.api.core.services.SwotAnalysisService;
import com.microservices.swotlyzer.api.core.services.SwotFieldService;
import com.microservices.swotlyzer.common.config.utils.WebClientUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.error.handling.OperationNotAllowedException;
import web.error.handling.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class SwotFieldServiceImpl implements SwotFieldService {
    private final SwotFieldRepository swotFieldRepository;

    private final SwotAnalysisService swotAnalysisService;

    private final SwotAnalysisRepository swotAnalysisRepository;

    private final HttpServletRequest httpServletRequest;

    public SwotFieldServiceImpl(SwotFieldRepository swotFieldRepository, SwotAnalysisService swotAnalysisService,
                                SwotAnalysisRepository swotAnalysisRepository, HttpServletRequest httpServletRequest) {
        this.swotFieldRepository = swotFieldRepository;
        this.swotAnalysisService = swotAnalysisService;
        this.swotAnalysisRepository = swotAnalysisRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public SwotField findById(String id) {
        return this.swotFieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SWOT Field not found."));
    }

    @Override
    public SwotField create(CreateSwotFieldDTO createSwotFieldDTO) {
        SwotAnalysis swotAnalysis = this.swotAnalysisService.findById(createSwotFieldDTO.getSwotAnalysisId());

        SwotField swotField = new SwotField();
        BeanUtils.copyProperties(createSwotFieldDTO, swotField);
        var userHeaderInfo = WebClientUtils.getUserHeadersInfo(httpServletRequest);

        swotField.setOwnerId(userHeaderInfo.getUserId());
        SwotField savedSwotField = this.swotFieldRepository.save(swotField);

        Map<String, List<SwotField>> swotLists =
                Map.ofEntries(Map.entry(SwotFieldType.WEAKNESS.name(), swotAnalysis.getWeaknesses()),
                        Map.entry(SwotFieldType.STRENGTH.name(), swotAnalysis.getStrengths()),
                        Map.entry(SwotFieldType.OPPORTUNITY.name(), swotAnalysis.getOpportunities()),
                        Map.entry(SwotFieldType.THREAT.name(), swotAnalysis.getThreats()));

        assignSwotField(swotLists, createSwotFieldDTO.getFieldLocation(), savedSwotField);
        swotAnalysisRepository.save(swotAnalysis);
        return savedSwotField;
    }

    private void assignSwotField(Map<String, List<SwotField>> map, String operationPosition, SwotField swotField) {
        map.get(operationPosition).add(swotField);
    }

    @Override
    public SwotField update(String swotFieldId, UpdateSwotFieldDTO updateSwotFieldDTO) {
        SwotField swotField = this.swotFieldRepository.findById(swotFieldId)
                .orElseThrow(() -> new ResourceNotFoundException("SWOT Field doesn't exist."));
        var userHeaderInfo = WebClientUtils.getUserHeadersInfo(httpServletRequest);
        Long currentUserId = userHeaderInfo.getUserId();
        if (!Objects.equals(swotField.getOwnerId(), currentUserId)) throw new OperationNotAllowedException(
                "SWOT Analysis with _id: " + swotFieldId + " does not belongs to the user with " + "_id: " +
                        currentUserId);
        BeanUtils.copyProperties(updateSwotFieldDTO, swotField);

        return this.swotFieldRepository.save(swotField);
    }

    @Override
    public SwotFieldDeleteResponse delete(String id) {

        SwotField swotField = this.swotFieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SWOT Field don't exist."));
        this.swotFieldRepository.delete(swotField);
        return new SwotFieldDeleteResponse("SWOT Field successfully deleted!");
    }
}
