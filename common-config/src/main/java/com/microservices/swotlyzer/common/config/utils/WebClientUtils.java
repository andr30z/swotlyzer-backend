package com.microservices.swotlyzer.common.config.utils;

import com.microservices.swotlyzer.common.config.dtos.UserHeaderInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Consumer;

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
        return new UserHeaderInfo(Long.parseLong(userIdHeader), userLoginHeader);
    }
}
