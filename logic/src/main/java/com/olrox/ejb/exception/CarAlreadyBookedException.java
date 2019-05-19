package com.olrox.ejb.exception;

public class CarAlreadyBookedException extends Exception{
    public CarAlreadyBookedException(String carNumber){
        super("Car " + carNumber + " already booked.");
    }
}
