package com.api.swotlyzer.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class User {
    @Id
    private String _id;
    private String email;
    private String name;
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date createdAt;
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date updatedAt;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
