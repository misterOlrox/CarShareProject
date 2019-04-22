package com.olrox.exception;

public class IllegalRoleException extends Exception{
    public IllegalRoleException(){
        super("User hasn't right role.");
    }
}
