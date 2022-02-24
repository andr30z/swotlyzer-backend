package com.api.swotlyzer.services;

import com.api.swotlyzer.dtos.CreateSwotFieldDTO;
import com.api.swotlyzer.dtos.SwotFieldDeleteResponse;
import com.api.swotlyzer.dtos.UpdateSwotFieldDTO;
import com.api.swotlyzer.models.SwotField;

public interface SwotFieldService {

    SwotField findById(String id);

    SwotField create(CreateSwotFieldDTO createSwotFieldDTO);

    SwotField update(String swotFieldId, UpdateSwotFieldDTO updateSwotFieldDTO);

    SwotFieldDeleteResponse delete(String id);

}
