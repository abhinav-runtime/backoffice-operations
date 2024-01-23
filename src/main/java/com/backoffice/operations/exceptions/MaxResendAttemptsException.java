package com.backoffice.operations.exceptions;

public class MaxResendAttemptsException extends RuntimeException {
    public MaxResendAttemptsException(String message) {
        super(message);
    }
}
