package com.api.swotlyzer.services;

import com.api.swotlyzer.dtos.CreateUserDTO;
import com.api.swotlyzer.dtos.LoginRequest;
import com.api.swotlyzer.dtos.LoginResponse;
import com.api.swotlyzer.models.User;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    User create(CreateUserDTO userDTO);

    User findById(String id);

    User me();

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken);
}
