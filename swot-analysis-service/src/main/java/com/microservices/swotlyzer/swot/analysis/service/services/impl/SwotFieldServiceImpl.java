package com.microservices.swotlyzer.swot.analysis.service.services.impl;

import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotFieldDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.SwotFieldDeleteResponse;
import com.microservices.swotlyzer.swot.analysis.service.dtos.UpdateSwotFieldDTO;
import com.microservices.swotlyzer.swot.analysis.service.enums.SwotFieldType;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotArea;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotField;
import com.microservices.swotlyzer.swot.analysis.service.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.swot.analysis.service.repositories.SwotFieldRepository;
import com.microservices.swotlyzer.swot.analysis.service.services.SwotAnalysisService;
import com.microservices.swotlyzer.swot.analysis.service.services.SwotFieldService;
import com.microservices.swotlyzer.common.config.utils.WebClientUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import web.error.handling.BadRequestException;
import web.error.handling.OperationNotAllowedException;
import web.error.handling.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
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

        Map<SwotFieldType, SwotArea> swotAreasMap =
                Map.ofEntries(Map.entry(SwotFieldType.WEAKNESS, swotAnalysis.getWeaknesses()),
                        Map.entry(SwotFieldType.STRENGTH, swotAnalysis.getStrengths()),
                        Map.entry(SwotFieldType.OPPORTUNITY, swotAnalysis.getOpportunities()),
                        Map.entry(SwotFieldType.THREAT, swotAnalysis.getThreats()));

        assignSwotField(swotAreasMap, createSwotFieldDTO.getFieldLocation(), savedSwotField);
        swotAnalysisRepository.save(swotAnalysis);
        return savedSwotField;
    }

    private void assignSwotField(Map<SwotFieldType, SwotArea> map, String operationPosition,
                                 SwotField swotField) {
        try {
            SwotArea area = map.get(SwotFieldType.valueOf(operationPosition));
            area.getFields().add(swotField);
        } catch (IllegalArgumentException _e) {
            throw new BadRequestException("Swot field location doesn't exists!");
        }
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
