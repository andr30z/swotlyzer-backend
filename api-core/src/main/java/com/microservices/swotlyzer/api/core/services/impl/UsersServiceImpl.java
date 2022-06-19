package com.microservices.swotlyzer.api.core.services.impl;

import com.microservices.swotlyzer.api.core.dtos.*;
import com.microservices.swotlyzer.api.core.exceptions.EntityExistsException;
import com.microservices.swotlyzer.api.core.exceptions.ResourceNotFoundException;
import com.microservices.swotlyzer.api.core.models.User;
import com.microservices.swotlyzer.api.core.repositories.UsersRepository;
import com.microservices.swotlyzer.api.core.services.TokenProvider;
import com.microservices.swotlyzer.api.core.services.UsersService;
import com.microservices.swotlyzer.api.core.utils.CookieUtil;
import com.microservices.swotlyzer.common.config.dtos.EmailDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Email;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    private final TokenProvider tokenProvider;

    private final CookieUtil cookieUtil;

    private final WebClient webClient;

    public UsersServiceImpl(UsersRepository usersRepository, TokenProvider tokenProvider, CookieUtil cookieUtil,
                            WebClient webClient) {
        this.usersRepository = usersRepository;
        this.tokenProvider = tokenProvider;
        this.cookieUtil = cookieUtil;
        this.webClient = webClient;
    }


    public void sendCreatedUserMail(User user) {
        String EMAIL_FROM = "no.reply.swotlyzer@gmail.com";
        String WELCOME = "Welcome";
        String CONTENT = "Welcome to our app!";
        EmailDTO emailDTO =
                EmailDTO.builder().ownerRef(user.get_id()).subject(WELCOME)
                        .content(CONTENT).emailFrom(EMAIL_FROM)
                        .emailTo(user.getEmail()).build();
        try {
            this.webClient.post().uri("http://localhost:9090/api/v1/email/send")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(BodyInserters.fromValue(emailDTO)).retrieve().bodyToMono(Object.class).block();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public User create(CreateUserDTO userDTO) {
        Optional<User> userOptional = this.usersRepository.findUserByEmail(userDTO.getEmail());
        if (userOptional.isPresent())
            throw new EntityExistsException("User with email: " + userDTO.getEmail() + " already exists.");
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User createdUser = this.usersRepository.save(user);
        this.sendCreatedUserMail(createdUser);
        return createdUser;
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken) {
        String email = loginRequest.getEmail();
        User user = this.findByEmail(email);
        Boolean accessTokenValid = tokenProvider.validateToken(accessToken);
        Boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);

        HttpHeaders responseHeaders = new HttpHeaders();
        Token newAccessToken;
        Token newRefreshToken;
        if (!accessTokenValid && !refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getEmail());
            newRefreshToken = tokenProvider.generateRefreshToken(user.getEmail());
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }

        if (!accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getEmail());
            addAccessTokenCookie(responseHeaders, newAccessToken);
        }

        if (accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getEmail());
            newRefreshToken = tokenProvider.generateRefreshToken(user.getEmail());
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS,
                "Auth successful. " + "Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);

    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken) {
        Boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);
        if (!refreshTokenValid) {
            throw new IllegalArgumentException("Refresh Token is invalid!");
        }

        String currentUserEmail = tokenProvider.getUsernameFromToken(refreshToken);
        System.out.println(currentUserEmail);

        Token newAccessToken = tokenProvider.generateAccessToken(currentUserEmail);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration())
                        .toString());

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS,
                "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    @Override
    public User findById(String id) {
        return this.usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public User me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return this.findByEmail(customUserDetails.getUsername());
    }

    private User findByEmail(String email) {
        return usersRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + " with email " + email));
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }
}
