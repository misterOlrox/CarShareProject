package com.olrox.ejb.carinteraction;

import com.olrox.domain.car.Car;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class CarBeeper {
    public void beep(Car car){
        long id = car.getId();
        System.out.println("Car "+ id + " says: beep-beep.");
    }
}
