package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.domain.account.RentalUser;
import com.olrox.domain.car.Car;
import com.olrox.ejb.exception.TooManyActiveOrdersException;
import com.olrox.domain.order.CarOrder;
import com.olrox.domain.order.OrderStatus;
import com.olrox.ejb.CarOrdersManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class CurrentOrderBean implements Serializable {
    @EJB
    private CarOrdersManager carOrdersManager;

    @Inject
    private CurrentSessionBean currentSessionBean;

    private CarOrder currentOrder;

    private Car currentCar;

    public CarOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(CarOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Car getCurrentCar() {
        return currentCar;
    }

    public boolean isActive(){
        if(currentOrder!=null){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isBooking(){
        if(currentOrder==null){
            return false;
        }
        else {
            return currentOrder.getOrderStatus() == OrderStatus.BOOKING;
        }
    }

    public boolean isRide(){
        if(currentOrder==null){
            return false;
        }
        else {
            return currentOrder.getOrderStatus() == OrderStatus.RIDE;
        }
    }

    public boolean isRideOver(){
        if(currentOrder==null){
            return false;
        }
        else {
            return currentOrder.getOrderStatus() == OrderStatus.RIDE_OVER;
        }
    }

    @PostConstruct
    public void init(){
        RentalUser user = currentSessionBean.getCurrentUser();
        try {
            currentOrder = carOrdersManager.getActiveOrder(user.getId());
        } catch (TooManyActiveOrdersException e) {
            addFatalMessage(e.getMessage());
            return;
        }

        if(currentOrder != null){
            currentCar = currentOrder.getCar();
        }
    }

    private void addFatalMessage(String message){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
                "Fatal error.", message));
    }

    public String cancel(){
        carOrdersManager.cancelOrder(currentOrder.getId());
        return "current-order.xhtml?faces-redirect=true";
    }
}
