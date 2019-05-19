package com.olrox.ejb.exception;

public class IllegalRoleException extends Exception{
    public IllegalRoleException(){
        super("User hasn't right role.");
    }
}
