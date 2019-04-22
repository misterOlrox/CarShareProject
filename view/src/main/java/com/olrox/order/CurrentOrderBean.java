package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.account.domain.RentalUser;
import com.olrox.account.ejb.RentalUsersManager;
import com.olrox.order.domain.CarOrder;
import com.olrox.order.domain.OrderStatus;
import com.olrox.order.ejb.CarOrdersManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class CurrentOrderBean implements Serializable {
    @EJB
    CarOrdersManager carOrdersManager;

    @EJB
    RentalUsersManager rentalUsersManager;

    @Inject
    CurrentSessionBean currentSessionBean;

    CarOrder currentOrder;

    boolean valid;

    public CarOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(CarOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    @PostConstruct
    public void update(){
        System.out.println("Valided");
        /*RentalUser user = currentSessionBean.getCurrentUser();
        currentOrder = carOrdersManager.find(user.);
        OrderStatus status = currentOrder.getOrderStatus();
        if(    status == OrderStatus.BOOKED
            || status == OrderStatus.RIDE
            || status == OrderStatus.RIDE_OVER){
            valid = true;
        }
        else{
            valid = false;
        }*/
    }

    public boolean isValid() {
        return valid;
    }
}
