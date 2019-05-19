package com.olrox.ejb.exception;

public class DuplicateCredentialsLoginException extends Exception{
    public DuplicateCredentialsLoginException(String login){
        super("Login " + login + " is already taken.");
    }
}
