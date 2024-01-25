package com.backoffice.operations.exceptions;

public class PasscodeLockedException extends RuntimeException {
    public PasscodeLockedException(String message) {
        super(message);
    }
}
