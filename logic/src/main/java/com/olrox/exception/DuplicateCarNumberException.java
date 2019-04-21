package com.olrox.exception;

public class DuplicateCarNumberException extends Exception{
    public DuplicateCarNumberException(String carNumber){
        super("Car with number " + carNumber + " already exists.");
    }
}
