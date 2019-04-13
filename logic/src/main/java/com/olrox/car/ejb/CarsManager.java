package com.olrox.car.ejb;

import com.olrox.car.domain.Car;
import com.olrox.car.domain.Coordinates;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class CarsManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public Car createCar(String carNumber, double lat, double lon){
        Car car = new Car();
        car.setCarNumber(carNumber);
        car.setFuel(100);
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(lat);
        coordinates.setLongitude(lon);
        car.setCoordinates(coordinates);

        entityManager.persist(coordinates);
        entityManager.persist(car);

        return car;
    }

    public List<Car> getAllCars(){
        TypedQuery<Car> namedQuery = entityManager.createNamedQuery("Car.getAll", Car.class);
        return namedQuery.getResultList();
    }


}