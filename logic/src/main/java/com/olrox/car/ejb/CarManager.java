package com.olrox.car.ejb;

import com.olrox.car.domain.Car;
import com.olrox.car.domain.Coordinates;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class CarManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public Car createCar(String carNumber, int lat, int lon){
        Car car = new Car();
        car.setCarNumber("1111 ab");
        car.setFuel(100);
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(lat);
        coordinates.setLongitude(lon);
        car.setCoordinates(coordinates);

        entityManager.persist(car);

        return car;
    }


}
