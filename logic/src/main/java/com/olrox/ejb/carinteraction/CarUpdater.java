package com.olrox.ejb.carinteraction;

import com.olrox.domain.car.Car;
import com.olrox.domain.car.Coordinates;
import com.olrox.ejb.CarsManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Random;

@Stateless
@LocalBean
public class CarUpdater {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    @EJB
    private CarsManager carsManager;

    public void updateCarInfo(long carId){
        Car car = carsManager.find(carId);
        Random random = new Random();
        car.setFuel(1+random.nextInt(100));
        Coordinates coordinates = car.getCoordinates();
        double rangeMin = -0.05;
        double rangeMax = 0.05;
        coordinates.setLatitude(coordinates.getLatitude() + rangeMin + (rangeMax - rangeMin) * random.nextDouble());
        coordinates.setLongitude(coordinates.getLongitude() + rangeMin + (rangeMax - rangeMin) * random.nextDouble());
        entityManager.merge(coordinates);
        entityManager.merge(car);
    }
}
