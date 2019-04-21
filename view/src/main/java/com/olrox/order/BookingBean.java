package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.car.domain.Car;
import com.olrox.exception.IllegalRoleException;
import com.olrox.map.CarSelectBean;
import com.olrox.order.ejb.CarOrdersManager;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class BookingBean implements Serializable {
    @EJB
    CarOrdersManager carOrdersManager;

    @Inject
    CarSelectBean carSelectBean;

    @Inject
    CurrentSessionBean currentSessionBean;

    public void book(){
        Car car = carSelectBean.getCar();
        try {
            carOrdersManager.createBookingOrder(car, currentSessionBean.getLogin());
        } catch (IllegalRoleException e) {
            e.printStackTrace();
        }
    }
}
