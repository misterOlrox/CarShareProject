package com.olrox.exception;

public class IllegalRoleException extends Exception{
    public IllegalRoleException(String username){
        super("User + " + username + " hasn't right role.");
    }
}
