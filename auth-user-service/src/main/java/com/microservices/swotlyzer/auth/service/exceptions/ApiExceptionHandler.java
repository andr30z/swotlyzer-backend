package com.microservices.swotlyzer.auth.service.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import web.error.handling.BaseApiExceptionHandler;
import web.error.handling.OperationNotAllowedException;

import java.util.Collections;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler extends BaseApiExceptionHandler {

    @ExceptionHandler(SecurityCipherDecodeException.class)
    public ResponseEntity<Object> handleSecurityCipherDecodeException(SecurityCipherDecodeException exception) {
        return this.buildResponseEntity(HttpStatus.UNAUTHORIZED, exception.getMessage(),
                Collections.singletonList(exception.getMessage()));
    }
}
