package com.microservices.swotlyzer.swot.analysis.service.dtos;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateSwotAnalysisDTO extends CreateSwotAnalysisDTO {
    @NotBlank
    private String _id;
}
