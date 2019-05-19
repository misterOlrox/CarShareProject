package com.olrox.domain.car;

import com.olrox.domain.car.Car;

import javax.persistence.*;

@Entity
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private double latitude;

    private double longitude;

    @OneToOne(mappedBy = "coordinates")
    private Car car;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
