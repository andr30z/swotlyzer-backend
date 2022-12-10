package com.microservices.swotlyzer.common.config.utils;

import java.util.function.Consumer;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

import com.microservices.swotlyzer.common.config.dtos.UserHeaderInfo;

public class WebClientUtils {

    public static final String X_AUTH_USER_ID = "X-auth-user-id";
    public static final String X_AUTH_USER_LOGIN = "X-auth-user-login";

    public static Consumer<HttpHeaders> setAuthHttpHeaders(HttpServletRequest httpServletRequest) {
        return httpHeaders -> {
            var userHeaders = getUserHeadersInfo(httpServletRequest);
            httpHeaders.add(X_AUTH_USER_LOGIN, userHeaders.getUsername());
            httpHeaders.add(X_AUTH_USER_ID, String.valueOf(userHeaders.getUserId()));
        };
    }

    public static UserHeaderInfo getUserHeadersInfo(HttpServletRequest httpServletRequest) {
        var userIdHeader = httpServletRequest.getHeader(X_AUTH_USER_ID);
        var userLoginHeader = httpServletRequest.getHeader(X_AUTH_USER_LOGIN);
        return new UserHeaderInfo(userIdHeader != null ? Long.parseLong(userIdHeader) : null, userLoginHeader);
    }
}
