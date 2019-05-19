package com.olrox.ejb.exception;

public class DuplicateCarNumberException extends Exception{
    public DuplicateCarNumberException(String carNumber){
        super("Car with number " + carNumber + " already exists.");
    }
}
