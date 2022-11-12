package com.microservices.swotlyzer.swot.analysis.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
public class UpdateSwotAnalysisDTO extends CreateSwotAnalysisDTO {
    @NotBlank
    private String _id;
}
