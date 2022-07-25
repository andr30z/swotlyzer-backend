package com.microservices.swotlyzer.api.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

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

    @DBRef
    private List<SwotField> strengths;
    @DBRef
    private List<SwotField> weaknesses;
    @DBRef
    private List<SwotField> opportunities;
    @DBRef
    private List<SwotField> threats;

    @NotNull
    private Long ownerId;


    public void setSwotLayoutType(String literalString) {
        this.swotLayoutType = SwotLayoutTypes.valueOf(literalString);
    }

}
