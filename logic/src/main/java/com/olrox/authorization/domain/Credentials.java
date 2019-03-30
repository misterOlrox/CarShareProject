package com.olrox.authorization.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Credentials {
    @Id
    private String login;

    private String password;

    @OneToOne(mappedBy = "credentials")
    private RentalUser rentalUser;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RentalUser getRentalUser() {
        return rentalUser;
    }

    public void setRentalUser(RentalUser rentalUser) {
        this.rentalUser = rentalUser;
    }
}
