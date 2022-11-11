package com.microservices.swotlyzer.swot.analysis.service.dtos;

import com.microservices.swotlyzer.swot.analysis.service.models.SwotLayoutTypes;
import com.microservices.swotlyzer.swot.analysis.service.validations.ValueOfEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
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
