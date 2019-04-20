package com.olrox.car.ejb;

import com.olrox.car.domain.Car;
import com.olrox.car.domain.Coordinates;
import com.olrox.car.domain.Model;
import com.olrox.car.domain.Status;

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

    public Car create(String carNumber, double lat, double lon, Model model) {
        if(isDuplicate(carNumber)){
            return null;
        }

        Car car = new Car();
        car.setCarNumber(carNumber);
        car.setFuel(100);
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(lat);
        coordinates.setLongitude(lon);
        car.setCoordinates(coordinates);
        car.setStatus(Status.FREE);
        car.setModel(model);

        entityManager.persist(coordinates);
        entityManager.persist(car);

        return car;
    }

    private boolean isDuplicate(String value) {
        if(entityManager.createQuery("from Car as c where c.carNumber=:value")
                .setParameter("value", value).getResultList().size()>0){
            return true;
        }
        else {
            return false;
        }
    }

    public List<Car> getAll() {
        TypedQuery<Car> namedQuery = entityManager.createNamedQuery("Car.getAll", Car.class);
        return namedQuery.getResultList();
    }


}
