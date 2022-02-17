package com.api.swotlyzer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@Data
public class LoginRequest {
    @NotEmpty(message = "Email address cannot be empty")
    @Email(message = "Please provide valid email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
