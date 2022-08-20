package com.microservices.swotlyzer.auth.service.services.impl;

import com.microservices.swotlyzer.auth.service.dtos.CreateUserDTO;
import com.microservices.swotlyzer.auth.service.dtos.LoginRequest;
import com.microservices.swotlyzer.auth.service.dtos.LoginResponse;
import com.microservices.swotlyzer.auth.service.models.Token;
import com.microservices.swotlyzer.auth.service.models.User;
import com.microservices.swotlyzer.auth.service.repositories.UserRepository;
import com.microservices.swotlyzer.auth.service.services.TokenProvider;
import com.microservices.swotlyzer.auth.service.services.UserService;
import com.microservices.swotlyzer.auth.service.utils.CookieUtil;
import com.microservices.swotlyzer.common.config.dtos.EmailDTO;
import com.microservices.swotlyzer.common.config.utils.WebClientUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import web.error.handling.BadRequestException;
import web.error.handling.EntityExistsException;
import web.error.handling.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Optional;

@Service
@ComponentScan(basePackages = {"com.microservices.swotlyzer"})
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;
    private final HttpServletRequest httpServletRequest;
    private final WebClient.Builder webClientBuilder;

    private final PasswordEncoder passwordEncoder;
    private final CookieUtil cookieUtil;

    public UserServiceImpl(UserRepository userRepository, TokenProvider tokenProvider, CookieUtil cookieUtil,
                           WebClient.Builder webClientBuilder, HttpServletRequest httpServletRequest, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.httpServletRequest = httpServletRequest;
        this.cookieUtil = cookieUtil;
        this.webClientBuilder = webClientBuilder;
        this.passwordEncoder = passwordEncoder;
    }


    private void sendCreatedUserMail(User user) {
        String EMAIL_FROM = "no.reply.swotlyzer@gmail.com";
        String WELCOME = "Welcome";
        String CONTENT = "Welcome to our app!";
        EmailDTO emailDTO = EmailDTO.builder().ownerRef(user.getId().toString()).subject(WELCOME).content(CONTENT)
                .emailFrom(EMAIL_FROM).emailTo(user.getEmail()).build();
        try {
            this.webClientBuilder.build().post().uri("http://mail-sender-service/api/v1/email/send")
                    .headers(WebClientUtils.setAuthHttpHeaders(httpServletRequest))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(BodyInserters.fromValue(emailDTO)).retrieve().bodyToMono(Object.class).block();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public User create(CreateUserDTO userDTO) {
        Optional<User> userOptional = this.userRepository.findUserByEmail(userDTO.getEmail());
        if (userOptional.isPresent())
            throw new EntityExistsException("User with email: " + userDTO.getEmail() + " already exists.");
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User createdUser = this.userRepository.save(user);
        sendCreatedUserMail(createdUser);
        return createdUser;
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken) {
        String email = loginRequest.getEmail();
        User user = this.findByEmail(email);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new BadRequestException("Password doesn't match!");
        var accessTokenValid = tokenProvider.validateToken(accessToken);
        var refreshTokenValid = tokenProvider.validateToken(refreshToken);

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
                "Auth successful. Tokens are created in cookies.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);

    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken) {
        var refreshTokenValid = tokenProvider.validateToken(refreshToken);
        if (!refreshTokenValid) throw new IllegalArgumentException("Refresh Token is invalid!");


        String currentUserEmail = tokenProvider.getUsernameFromToken(refreshToken);
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
    public User getTokenUser(String token) {
        boolean isTokenValid = this.tokenProvider.validateToken(token);
        if (!isTokenValid) throw new ResourceNotFoundException("");
        var tokenUsername = this.tokenProvider.getUsernameFromToken(token);
        return this.userRepository.findUserByEmail(tokenUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public User me() {
        var userIdHeader = httpServletRequest.getHeader(WebClientUtils.X_AUTH_USER_ID);
        if (userIdHeader == null || userIdHeader.trim().length() == 0)
            throw new BadRequestException("No user ID headers found!");
        Long currentUserId = Long.parseLong(userIdHeader);
        return this.findById(currentUserId);
    }

    private User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
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
