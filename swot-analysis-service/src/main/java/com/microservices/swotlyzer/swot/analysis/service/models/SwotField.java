package com.microservices.swotlyzer.swot.analysis.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class SwotField extends BaseEntity{

    @Id
    private String _id;

    private String text;
    private Integer fontSize;
    private String fontFamily;
    private String fontWeight;
    private String color;
    private String fontStyle;
    private Long ownerId;
}
