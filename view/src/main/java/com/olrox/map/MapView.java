package com.olrox.map;

import com.olrox.car.domain.Car;
import com.olrox.car.ejb.CarsManager;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class MapView implements Serializable {
    private MapModel simpleModel;

    private Marker marker;

    private String carNumber;

    private double lat;

    private double lng;

    @EJB
    private CarsManager carsManager;

    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();

        List<Car> cars = carsManager.getAll();
        for(Car car: cars){
            LatLng coord = new LatLng(  car.getCoordinates().getLatitude(),
                                        car.getCoordinates().getLongitude());
            simpleModel.addOverlay(new Marker(coord, car.getCarNumber()));
        }
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void addMarker() {
        Marker marker = new Marker(new LatLng(lat, lng), carNumber);
        simpleModel.addOverlay(marker);
        carsManager.create(carNumber, lat, lng);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));
    }

    public void setSimpleModel(MapModel simpleModel) {
        this.simpleModel = simpleModel;
    }

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

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        this.marker = (Marker) event.getOverlay();
    }
}