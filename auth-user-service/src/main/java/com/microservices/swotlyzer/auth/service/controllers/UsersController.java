package com.microservices.swotlyzer.auth.service.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.swotlyzer.auth.service.dtos.CreateUserDTO;
import com.microservices.swotlyzer.auth.service.dtos.LoginRequest;
import com.microservices.swotlyzer.auth.service.dtos.LoginResponse;
import com.microservices.swotlyzer.auth.service.models.User;
import com.microservices.swotlyzer.auth.service.services.UserService;
import com.microservices.swotlyzer.auth.service.utils.SecurityCipher;

@RestController
@RequestMapping("/api/v1/auth-users")
public class UsersController {
    private final UserService usersService;

    public UsersController(UserService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/users")
    public User create(@RequestBody @Validated CreateUserDTO createUserDTO) {
        return this.usersService.create(createUserDTO);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody @Validated LoginRequest loginRequest) {

        return usersService.login(loginRequest, accessToken, refreshToken);
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> refreshToken(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return usersService.refresh(accessToken, refreshToken);
    }

    @GetMapping("/me")
    public User me() {
        return this.usersService.me();
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable Long id) {
        return usersService.findById(id);
    }

    @GetMapping("/validate-token")
    public User validateToken(@RequestParam String token) {
        return this.usersService.getTokenUser(token);
    }

}
