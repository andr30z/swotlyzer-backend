package com.microservices.swotlyzer.swot.analysis.service.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateSwotFieldDTO extends  CreateSwotFieldDTO {
    @NotBlank
    private String _id;
}
