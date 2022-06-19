package com.swotlyzer.api.core.services;

import com.swotlyzer.api.core.dtos.CreateUserDTO;
import com.swotlyzer.api.core.dtos.LoginRequest;
import com.swotlyzer.api.core.dtos.LoginResponse;
import com.swotlyzer.api.core.models.User;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    User create(CreateUserDTO userDTO);

    User findById(String id);

    User me();

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken);
}
