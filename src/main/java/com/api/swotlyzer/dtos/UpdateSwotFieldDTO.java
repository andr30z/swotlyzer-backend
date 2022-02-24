package com.api.swotlyzer.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateSwotFieldDTO extends  CreateSwotFieldDTO {
    @NotBlank
    private String _id;
}
