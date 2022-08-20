package com.microservices.swotlyzer.auth.service.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@Data
@AllArgsConstructor
public class LoginRequest {
    @NotEmpty(message = "Email address cannot be empty")
    @Email(message = "Please provide valid email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
