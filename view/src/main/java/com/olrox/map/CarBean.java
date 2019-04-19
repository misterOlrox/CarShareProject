package com.olrox.map;

import com.olrox.admin.ModelCollectorView;
import com.olrox.car.domain.Car;
import com.olrox.car.domain.Model;
import com.olrox.car.ejb.CarsManager;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CarBean implements Serializable {
    private Marker marker;

    @Inject
    private MapView mapView;

    @EJB
    private CarsManager carsManager;

    private String carNumber;

    private double lat;

    private double lng;

    private Model model;

    private int modelId;




    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
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

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        this.marker = (Marker) event.getOverlay();
    }

    public void addNewCar() {
        Car car = carsManager.create(carNumber, lat, lng, model);
        Marker marker = new Marker(new LatLng(lat, lng), carNumber, car.getId());
        mapView.getSimpleModel().addOverlay(marker);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Marker Added",
                        "Lat:" + lat + ", Lng:" + lng));
    }
}
