package com.olrox.ejb.exception;

public class CarNotBookedException extends Exception{
    public CarNotBookedException(String carId){
        super("Car "+ carId +" not booked.");
    }
}
