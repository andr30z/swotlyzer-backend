package com.microservices.swotlyzer.swot.analysis.service.models;

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
