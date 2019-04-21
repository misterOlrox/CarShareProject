package com.olrox.account.domain;

import com.olrox.order.domain.CarOrder;

import javax.persistence.*;
import java.util.List;

@Entity
public class RentalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    @OneToOne
    private Credentials credentials;

    private String email;

    private String phoneNumber;

    @OneToMany(mappedBy = "rentalUser")
    private List<CarOrder> carOrder;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<CarOrder> getCarOrder() {
        return carOrder;
    }

    public void setCarOrder(List<CarOrder> carOrder) {
        this.carOrder = carOrder;
    }
}
