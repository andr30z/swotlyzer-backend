package com.microservices.swotlyzer.swot.analysis.service.dtos;

import com.microservices.swotlyzer.swot.analysis.service.enums.SwotFieldType;
import com.microservices.swotlyzer.swot.analysis.service.validations.ValueOfEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
public class CreateSwotFieldDTO {
    @NotNull(message = "is required")
    @NotBlank
    private String swotAnalysisId;
    @NotNull(message = "is required")
    @NotBlank
    private String text;
    @NotNull(message = "is required")
    private Integer fontSize;
    @NotNull(message = "is required")
    @NotBlank
    private String fontFamily;
    @NotNull(message = "is required")
    @NotBlank
    private String fontWeight;
    @NotNull(message = "is required")
    @NotBlank
    private String fontStyle;
    @NotNull(message = "is required")
    @NotBlank
    private String color;

    @ValueOfEnum(enumClass = SwotFieldType.class)
    @NotNull(message = "is required")
    private String fieldLocation;
}
