package com.swotlyzer.api.core.services.impl;

import com.swotlyzer.api.core.dtos.CreateSwotAnalysisDTO;
import com.swotlyzer.api.core.dtos.PaginationResponse;
import com.swotlyzer.api.core.models.SwotAnalysis;
import com.swotlyzer.api.core.models.User;
import com.swotlyzer.api.core.repositories.SwotAnalysisRepository;
import com.swotlyzer.api.core.services.SwotAnalysisService;
import com.swotlyzer.api.core.utils.PaginationUtil;
import com.swotlyzer.api.core.dtos.UpdateSwotAnalysisDTO;
import com.swotlyzer.api.core.exceptions.OperationNotAllowedException;
import com.swotlyzer.api.core.exceptions.ResourceNotFoundException;
import com.swotlyzer.api.core.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SwotAnalysisServiceImpl implements SwotAnalysisService {
    @Autowired
    private SwotAnalysisRepository swotAnalysisRepository;

    @Autowired
    private UsersService usersService;

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
                "SWOT Analysis with _id: " + swotAnalysisId +
                        " don't belongs to the user with " +
                        "_id: " + currentUser.get_id());
        swotAnalysis.setSwotLayoutType(updateSWOTAnalysisDTO.getLayoutType());
        BeanUtils.copyProperties(updateSWOTAnalysisDTO, swotAnalysis);


        return this.swotAnalysisRepository.save(swotAnalysis);
    }
}