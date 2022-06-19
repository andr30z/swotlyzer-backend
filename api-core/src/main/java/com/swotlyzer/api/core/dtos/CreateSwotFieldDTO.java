package com.swotlyzer.api.core.dtos;

import com.swotlyzer.api.core.validations.ValueOfEnum;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateSwotFieldDTO {
    @NotNull(message = "is required")
    private String swotAnalysisId;
    @NotNull(message = "is required")
    private String text;
    @NotNull(message = "is required")
    private int fontSize;
    @NotNull(message = "is required")
    private String fontFamily;
    @NotNull(message = "is required")
    private String fontWeight;
    @NotNull(message = "is required")
    private String color;

    public enum SwotFieldType{
        WEAKNESS,
        STRENGTH,
        OPPORTUNITY,
        THREAT
    }

    @ValueOfEnum(enumClass = SwotFieldType.class)
    @NotNull(message = "is required")
    private String fieldLocation;
}
