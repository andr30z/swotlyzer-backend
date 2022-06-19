package com.swotlyzer.api.core.services;


import com.swotlyzer.api.core.dtos.CreateSwotFieldDTO;
import com.swotlyzer.api.core.models.SwotField;
import com.swotlyzer.api.core.dtos.SwotFieldDeleteResponse;
import com.swotlyzer.api.core.dtos.UpdateSwotFieldDTO;

public interface SwotFieldService {

    SwotField findById(String id);

    SwotField create(CreateSwotFieldDTO createSwotFieldDTO);

    SwotField update(String swotFieldId, UpdateSwotFieldDTO updateSwotFieldDTO);

    SwotFieldDeleteResponse delete(String id);

}
