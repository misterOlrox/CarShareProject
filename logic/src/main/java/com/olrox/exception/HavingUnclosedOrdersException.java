package com.olrox.exception;

public class HavingUnclosedOrdersException extends Exception{
    public HavingUnclosedOrdersException(String user){
        super("User " + user + " has unclosed orders.");
    }
}
