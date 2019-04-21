package com.olrox.map;

import com.olrox.car.domain.Car;
import com.olrox.car.ejb.CarsManager;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Marker;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CarSelectBean implements Serializable {
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
