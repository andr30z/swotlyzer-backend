package com.api.swotlyzer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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

//    @DBRef
//    private List<SwotField> swotFields;

    @DBRef
    private User creator;


    public void setSwotLayoutType(String literalString) {
        this.swotLayoutType = SwotLayoutTypes.valueOf(literalString);
    }

}
