package com.switchfully.eurder.domain.exceptions;

public class ItemAlreadyExistsException extends RuntimeException{
    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
