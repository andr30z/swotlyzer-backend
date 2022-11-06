package com.microservices.swotlyzer.api.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

@Data
@AllArgsConstructor
public class SwotArea {
    private String title;
    private String backgroundColor;
    @DBRef
    private Set<SwotField> fields;
}
