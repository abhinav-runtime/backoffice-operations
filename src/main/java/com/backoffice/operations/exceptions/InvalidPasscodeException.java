package com.backoffice.operations.exceptions;

public class InvalidPasscodeException extends RuntimeException {
    public InvalidPasscodeException(String message) {
        super(message);
    }
}
