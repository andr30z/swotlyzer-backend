package com.microservices.swotlyzer.api.core.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
public class UpdateSwotAnalysisDTO extends CreateSwotAnalysisDTO {
    @NotBlank
    private String _id;
}
