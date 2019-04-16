package com.olrox.car.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String manufacturer;

    private String modelName;

    @Lob
    private byte[] image;

    private int pricePerMinute;

    @OneToMany(mappedBy = "carModel")
    private List<Car> car;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(int costPerMinute) {
        this.pricePerMinute = costPerMinute;
    }

    public List<Car> getCar() {
        return car;
    }

    public void setCar(List<Car> car) {
        this.car = car;
    }


}
