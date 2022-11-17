package com.microservices.swotlyzer.swot.analysis.service.services;


import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotFieldDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.SwotFieldDeleteResponse;
import com.microservices.swotlyzer.swot.analysis.service.dtos.UpdateSwotFieldDTO;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotField;

public interface SwotFieldService {

    SwotField findById(String id);

    SwotField create(CreateSwotFieldDTO createSwotFieldDTO);

    SwotField update(String swotFieldId, UpdateSwotFieldDTO updateSwotFieldDTO);

    SwotFieldDeleteResponse delete(String id);

}
