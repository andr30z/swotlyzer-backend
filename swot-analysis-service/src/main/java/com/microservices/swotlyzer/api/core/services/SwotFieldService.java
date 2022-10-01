package com.microservices.swotlyzer.api.core.services;


import com.microservices.swotlyzer.api.core.dtos.CreateSwotFieldDTO;
import com.microservices.swotlyzer.api.core.dtos.SwotFieldDeleteResponse;
import com.microservices.swotlyzer.api.core.dtos.UpdateSwotFieldDTO;
import com.microservices.swotlyzer.api.core.models.SwotField;

public interface SwotFieldService {

    SwotField findById(String id);

    SwotField create(CreateSwotFieldDTO createSwotFieldDTO);

    SwotField update(String swotFieldId, UpdateSwotFieldDTO updateSwotFieldDTO);

    SwotFieldDeleteResponse delete(String id);

}
