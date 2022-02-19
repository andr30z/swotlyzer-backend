package com.api.swotlyzer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class SwotField extends BaseEntity{

    @Id
    private String _id;
    private String text;
    private String fontFamily;
    private String fontWeight;
    private String color;

    @DBRef
    private User creator;
}
