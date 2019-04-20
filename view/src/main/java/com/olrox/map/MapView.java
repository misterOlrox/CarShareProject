package com.olrox.map;

import com.olrox.car.domain.Car;
import com.olrox.car.ejb.CarsManager;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class MapView implements Serializable {
    private MapModel simpleModel;

    @EJB
    private CarsManager carsManager;

    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();

        List<Car> cars = carsManager.getAll();
        for(Car car: cars){
            LatLng coord = new LatLng(  car.getCoordinates().getLatitude(),
                                        car.getCoordinates().getLongitude());
            simpleModel.addOverlay(new Marker(coord, car.getCarNumber(), car.getId()));
        }
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void setSimpleModel(MapModel simpleModel) {
        this.simpleModel = simpleModel;
    }
}
