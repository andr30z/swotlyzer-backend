package com.api.swotlyzer.services.impl;

import com.api.swotlyzer.dtos.CreateSwotFieldDTO;
import com.api.swotlyzer.dtos.SwotFieldDeleteResponse;
import com.api.swotlyzer.dtos.UpdateSwotFieldDTO;
import com.api.swotlyzer.exceptions.OperationNotAllowedException;
import com.api.swotlyzer.exceptions.ResourceNotFoundException;
import com.api.swotlyzer.models.SwotField;
import com.api.swotlyzer.models.User;
import com.api.swotlyzer.repositories.SwotFieldRepository;
import com.api.swotlyzer.services.SwotFieldService;
import com.api.swotlyzer.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwotFieldServiceImpl implements SwotFieldService {
    @Autowired
    private SwotFieldRepository swotFieldRepository;

    @Autowired
    private UsersService usersService;

    @Override
    public SwotField findById(String id) {
        return this.swotFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SWOT Field not found."));
    }

    @Override
    public SwotField create(CreateSwotFieldDTO createSwotFieldDTO) {
        SwotField swotField = new SwotField();
        BeanUtils.copyProperties(createSwotFieldDTO, swotField);
        User currentUser = this.usersService.me();

        swotField.setCreator(currentUser);
        return this.swotFieldRepository.save(swotField);
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

        SwotField swotField = this.swotFieldRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("SWOT Field don't exist."));
        this.swotFieldRepository.delete(swotField);
        return new SwotFieldDeleteResponse("SWOT Field successfully deleted!");
    }
}
