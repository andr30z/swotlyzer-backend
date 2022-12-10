package com.microservices.swotlyzer.auth.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 8, message = "should have at least 8 characters.")
    private String password;

    @NotBlank
    @Size(min = 7)
    private String phone;


}
