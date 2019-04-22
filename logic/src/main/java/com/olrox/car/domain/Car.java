package com.olrox.car.domain;

import com.olrox.order.domain.CarOrder;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedQuery(name = "Car.getAll", query = "SELECT c from Car c")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String carNumber;

    @ManyToOne
    private Model model;

    private int fuel;

    @OneToOne
    private Coordinates coordinates;

    @OneToMany(mappedBy = "car")
    private List<CarOrder> orders;

    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<CarOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CarOrder> orders) {
        this.orders = orders;
    }

    public CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
