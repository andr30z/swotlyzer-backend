package com.microservices.swotlyzer.auth.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${authentication.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${authentication.auth.secureToken:true}")
    private boolean secureToken;

    @Value("${authentication.auth.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    private ResponseCookie buildCookieConfig(String cookieSourceName, String token,
            Long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);

        ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(cookieSourceName, encryptedToken)
                .maxAge(duration)
                .httpOnly(true)
                .sameSite("None")
                .path("/");
        if (secureToken)
            responseCookieBuilder.secure(true);

        return responseCookieBuilder.build();
    }

    public HttpCookie createAccessTokenCookie(String token, Long duration) {
        return buildCookieConfig(accessTokenCookieName, token, duration);
    }

    public HttpCookie createRefreshTokenCookie(String token, Long duration) {
        return buildCookieConfig(refreshTokenCookieName, token, duration);
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(accessTokenCookieName, "").maxAge(0).httpOnly(true).path("/").build();
    }

}