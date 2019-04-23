package com.olrox.admin;

import com.olrox.car.domain.Car;
import com.olrox.car.domain.Model;
import com.olrox.car.ejb.CarsManager;
import com.olrox.exception.DuplicateCarNumberException;
import com.olrox.map.MapView;
import org.primefaces.PrimeFaces;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class NewCarBean implements Serializable {
    private Marker marker;

    @Inject
    private MapView mapView;

    @EJB
    private CarsManager carsManager;

    private String carNumber;

    private double lat;

    private double lng;

    private Model model;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber.toUpperCase();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void addNewCar() {
        if(model==null){
            addNoModelMessage();
            return;
        }
        Car car;
        try {
            car = carsManager.create(carNumber, lat, lng, model);
        } catch (DuplicateCarNumberException e) {
            addDuplicateCarMessage(e.getMessage());
            return;
        }
        Marker marker = new Marker(new LatLng(lat, lng), carNumber, car.getId());
        mapView.getSimpleModel().addOverlay(marker);
        addSuccessMessage();
        PrimeFaces.current().executeScript("PF('dlg').hide();");
    }

    private void addDuplicateCarMessage(String message){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Duplicate car", message));
    }

    private void addSuccessMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Car " +carNumber+ " Added", "Lat:" + lat + ", Lng:" + lng));
    }

    private void addNoModelMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Choose car's model", null));
    }
}
