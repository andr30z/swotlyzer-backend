package com.microservices.swotlyzer.swot.analysis.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.microservices.swotlyzer.swot.analysis.service.models.SwotLayoutTypes;
import com.microservices.swotlyzer.swot.analysis.service.validations.ValueOfEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreateSwotAnalysisDTO {

    @NotBlank(message = "is blank.")
    @NotNull(message = "is required")
    private String title;

    private String description;

    @NotNull(message = "is required.")
    private Boolean swotTemplate;

    @ValueOfEnum(enumClass = SwotLayoutTypes.class)
    @NotNull(message = "is required")
    private String layoutType;

}
