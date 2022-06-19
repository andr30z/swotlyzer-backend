package com.microservices.swotlyzer.api.core.services;


import com.microservices.swotlyzer.api.core.dtos.Token;

import java.time.LocalDateTime;


public interface TokenProvider {
    Token generateAccessToken(String subject);

    Token generateRefreshToken(String subject);

    String getUsernameFromToken(String token);

    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateToken(String token);
}
