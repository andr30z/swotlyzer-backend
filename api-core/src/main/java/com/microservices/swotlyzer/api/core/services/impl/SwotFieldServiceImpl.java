package com.microservices.swotlyzer.api.core.services.impl;

import com.microservices.swotlyzer.api.core.models.SwotAnalysis;
import com.microservices.swotlyzer.api.core.models.SwotField;
import com.microservices.swotlyzer.api.core.models.User;
import com.microservices.swotlyzer.api.core.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.api.core.services.SwotAnalysisService;
import com.microservices.swotlyzer.api.core.dtos.CreateSwotFieldDTO;
import com.microservices.swotlyzer.api.core.dtos.SwotFieldDeleteResponse;
import com.microservices.swotlyzer.api.core.dtos.UpdateSwotFieldDTO;
import com.microservices.swotlyzer.api.core.exceptions.OperationNotAllowedException;
import com.microservices.swotlyzer.api.core.exceptions.ResourceNotFoundException;
import com.microservices.swotlyzer.api.core.repositories.SwotFieldRepository;
import com.microservices.swotlyzer.api.core.services.SwotFieldService;
import com.microservices.swotlyzer.api.core.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SwotFieldServiceImpl implements SwotFieldService {
    @Autowired
    private SwotFieldRepository swotFieldRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private SwotAnalysisService swotAnalysisService;

    @Autowired
    private SwotAnalysisRepository swotAnalysisRepository;

    @Override
    public SwotField findById(String id) {
        return this.swotFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SWOT Field not found."));
    }

    @Override
    public SwotField create(CreateSwotFieldDTO createSwotFieldDTO) {
        SwotAnalysis swotAnalysis = this.swotAnalysisService.findById(createSwotFieldDTO.getSwotAnalysisId());

        SwotField swotField = new SwotField();
        BeanUtils.copyProperties(createSwotFieldDTO, swotField);
        User currentUser = this.usersService.me();

        swotField.setCreator(currentUser);
        SwotField savedSwotField = this.swotFieldRepository.save(swotField);
        Map<String, List<SwotField>> swotLists = new HashMap<String, List<SwotField>>() {{
            put("WEAKNESS", swotAnalysis.getSwotFieldWeaknesses());
            put("STRENGTH", swotAnalysis.getSwotFieldStrengths());
            put("OPPORTUNITY", swotAnalysis.getSwotFieldOpportunities());
            put("THREAT", swotAnalysis.getSwotFieldThreats());
        }};
        assignSwotField(swotLists, createSwotFieldDTO.getFieldLocation(), savedSwotField);
        swotAnalysisRepository.save(swotAnalysis);
        return savedSwotField;
    }

    private void assignSwotField(Map<String, List<SwotField>> map, String operationPosition, SwotField swotField) {
        map.get(operationPosition).add(swotField);
    }

    @Override
    public SwotField update(String swotFieldId, UpdateSwotFieldDTO updateSwotFieldDTO) {
        SwotField swotField = this.swotFieldRepository.findById(swotFieldId).orElseThrow(() -> new ResourceNotFoundException("SWOT Field don't exist."));
        User currentUser = this.usersService.me();
        if (!swotField.getCreator().get_id().equals(currentUser.get_id())) throw new OperationNotAllowedException(
                "SWOT Analysis with _id: " + swotFieldId +
                        " don't belongs to the user with " +
                        "_id: " + currentUser.get_id());
        BeanUtils.copyProperties(updateSwotFieldDTO, swotField);

        return this.swotFieldRepository.save(swotField);
    }

    @Override
    public SwotFieldDeleteResponse delete(String id) {

        SwotField swotField = this.swotFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SWOT Field don't exist."));
        this.swotFieldRepository.delete(swotField);
        return new SwotFieldDeleteResponse("SWOT Field successfully deleted!");
    }
}
