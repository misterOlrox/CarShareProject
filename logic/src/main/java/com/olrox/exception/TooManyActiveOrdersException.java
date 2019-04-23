package com.olrox.exception;

public class TooManyActiveOrdersException extends Exception{
    public TooManyActiveOrdersException(long userId, int quantity){
        super("User with id " + userId + " has " + quantity + " active orders.");
    }
}
