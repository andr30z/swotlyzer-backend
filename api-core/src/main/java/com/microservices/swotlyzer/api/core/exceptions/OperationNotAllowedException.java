package com.microservices.swotlyzer.api.core.exceptions;

public class OperationNotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OperationNotAllowedException(String message) {
        super(message);
    }
}
