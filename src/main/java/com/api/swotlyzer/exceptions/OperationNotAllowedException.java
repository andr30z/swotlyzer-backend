package com.api.swotlyzer.exceptions;

public class OperationNotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OperationNotAllowedException(String message) {
        super(message);
    }
}
