package com.microservices.swotlyzer.common.config.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
public class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
