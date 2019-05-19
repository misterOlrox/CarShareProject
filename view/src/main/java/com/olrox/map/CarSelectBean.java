package com.olrox.map;

import com.olrox.domain.car.Car;
import com.olrox.ejb.CarsManager;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Marker;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CarSelectBean implements Serializable {
    @EJB
    private CarsManager carsManager;

    private Car car;

    public Car getCar() {
        return car;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        Marker marker = (Marker) event.getOverlay();
        if (marker == null){
            addErrorMessage();
            car = null;
            return;
        }
        car = carsManager.find((long)marker.getData());
    }

    private void addErrorMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Sorry.", "This car seems to be already booked"));
    }
}
