package com.api.swotlyzer.dtos;

import com.api.swotlyzer.models.SwotLayoutTypes;
import com.api.swotlyzer.validations.ValueOfEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
