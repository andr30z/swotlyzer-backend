package com.microservices.swotlyzer.api.core.services.impl;

import com.microservices.swotlyzer.api.core.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.api.core.dtos.PaginationResponse;
import com.microservices.swotlyzer.api.core.models.SwotAnalysis;
import com.microservices.swotlyzer.api.core.models.User;
import com.microservices.swotlyzer.api.core.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.api.core.services.SwotAnalysisService;
import com.microservices.swotlyzer.api.core.utils.PaginationUtil;
import com.microservices.swotlyzer.api.core.dtos.UpdateSwotAnalysisDTO;
import com.microservices.swotlyzer.api.core.exceptions.OperationNotAllowedException;
import com.microservices.swotlyzer.api.core.exceptions.ResourceNotFoundException;
import com.microservices.swotlyzer.api.core.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Service
public class SwotAnalysisServiceImpl implements SwotAnalysisService {
    private final SwotAnalysisRepository swotAnalysisRepository;

    private final UsersService usersService;

    public SwotAnalysisServiceImpl(SwotAnalysisRepository swotAnalysisRepository,
                                   UsersService usersService
    ) {
        this.swotAnalysisRepository = swotAnalysisRepository;
        this.usersService = usersService;

    }

    @Override
    public SwotAnalysis findById(String id) {
        return this.swotAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SWOT Analysis not found."));
    }

    @Override
    public PaginationResponse<SwotAnalysis> findByCurrentUser(int page, int perPage) {
        Pageable paging = PageRequest.of(page - 1, perPage);

        Page<SwotAnalysis> swotPage = this.swotAnalysisRepository.findByCreator(this.usersService.me(), paging);
        return new PaginationUtil<SwotAnalysis, SwotAnalysisRepository>().buildResponse(swotPage);
    }

    @Override
    public SwotAnalysis create(CreateSwotAnalysisDTO createSWOTAnalysisDTO) {
        SwotAnalysis swotAnalysis = new SwotAnalysis();
        swotAnalysis.setSwotFieldWeaknesses(Collections.emptyList());
        swotAnalysis.setSwotFieldStrengths(Collections.emptyList());
        swotAnalysis.setSwotFieldThreats(Collections.emptyList());
        swotAnalysis.setSwotFieldOpportunities(Collections.emptyList());
        BeanUtils.copyProperties(createSWOTAnalysisDTO, swotAnalysis);
        User currentUser = this.usersService.me();
        swotAnalysis.setCreator(currentUser);
        swotAnalysis.setSwotLayoutType(createSWOTAnalysisDTO.getLayoutType());
        return this.swotAnalysisRepository.save(swotAnalysis);
    }

    @Override
    public SwotAnalysis update(String swotAnalysisId, UpdateSwotAnalysisDTO updateSWOTAnalysisDTO) {

        SwotAnalysis swotAnalysis = this.swotAnalysisRepository.findById(swotAnalysisId)
                .orElseThrow(() -> new ResourceNotFoundException("SWOT Analysis don't exist."));
        User currentUser = this.usersService.me();
        if (!swotAnalysis.getCreator().get_id().equals(currentUser.get_id())) throw new OperationNotAllowedException(
                "SWOT Analysis with _id: " + swotAnalysisId + " don't belongs to user with " + "id: " +
                        currentUser.get_id());
        BeanUtils.copyProperties(updateSWOTAnalysisDTO, swotAnalysis);
        swotAnalysis.setSwotLayoutType(updateSWOTAnalysisDTO.getLayoutType());


        return this.swotAnalysisRepository.save(swotAnalysis);
    }
}
