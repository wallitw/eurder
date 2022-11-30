package com.switchfully.eurder.domain.exceptions;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException() {
        super("Unauthorized");
    }

    public UnknownUserException(String message) {
        super(message);
    }
}