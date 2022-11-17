package com.microservices.swotlyzer.swot.analysis.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class SwotAnalysis extends BaseEntity {
    @Id
    private String _id;

    private String title;
    private SwotLayoutTypes swotLayoutType;
    private boolean swotTemplate;

    private SwotArea strengths;
    private SwotArea weaknesses;
    private SwotArea opportunities;
    private SwotArea threats;

    @NotNull
    private Long ownerId;


    public void setSwotLayoutType(String literalString) {
        this.swotLayoutType = SwotLayoutTypes.valueOf(literalString);
    }

}
