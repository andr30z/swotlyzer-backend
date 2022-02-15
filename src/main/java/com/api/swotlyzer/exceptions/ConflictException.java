package com.api.swotlyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConflictException(String message) {
        super(message);
    }
}
