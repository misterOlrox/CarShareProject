package com.olrox.map;

import com.olrox.car.domain.Car;
import com.olrox.car.ejb.CarsManager;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Marker;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class CarSelectBean {
    @EJB
    CarsManager carsManager;

    private Car car;

    public Car getCar() {
        return car;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        Marker marker = (Marker) event.getOverlay();
        car = carsManager.find((long)marker.getData());
    }
}
