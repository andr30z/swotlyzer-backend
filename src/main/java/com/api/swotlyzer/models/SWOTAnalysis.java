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
public class SWOTAnalysis {
    @Id
    private String _id;

    private String title;
    private SWOTLayoutTypes swotLayoutTypes;

    @DBRef
    private User creator;
}
