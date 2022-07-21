package com.microservices.swotlyzer.auth.service.services;

import com.microservices.swotlyzer.auth.service.dtos.CreateUserDTO;
import com.microservices.swotlyzer.auth.service.dtos.LoginRequest;
import com.microservices.swotlyzer.auth.service.dtos.LoginResponse;
import com.microservices.swotlyzer.auth.service.models.User;
import org.springframework.http.ResponseEntity;

public interface UserService {


    User create(CreateUserDTO userDTO);

    User findById(Long id);

    User me();


    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken);

    User getTokenUser(String token);
}
