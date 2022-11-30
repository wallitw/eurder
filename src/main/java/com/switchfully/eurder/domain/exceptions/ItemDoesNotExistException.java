package com.switchfully.eurder.domain.exceptions;

public class ItemDoesNotExistException extends RuntimeException{
    public ItemDoesNotExistException(String message) {
        super(message);
    }
}
