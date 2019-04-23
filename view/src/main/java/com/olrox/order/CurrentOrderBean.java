package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.account.domain.RentalUser;
import com.olrox.car.domain.Car;
import com.olrox.carinteraction.CarBeeper;
import com.olrox.exception.CarNotBookedException;
import com.olrox.exception.TooManyActiveOrdersException;
import com.olrox.order.domain.CarOrder;
import com.olrox.order.domain.OrderStatus;
import com.olrox.order.ejb.CarOrdersManager;

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
    CarOrdersManager carOrdersManager;

    @EJB
    CarBeeper carBeeper;

    @Inject
    CurrentSessionBean currentSessionBean;

    CarOrder currentOrder;

    Car currentCar;

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

    public String beep(){
        carBeeper.beep(currentOrder.getCar());
        addBeepMessage();
        return null;
    }

    private void addBeepMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Info.", "Your car beeped."));
    }

    public String startRide(){
        try {
            carOrdersManager.startRide(currentOrder.getId());
        } catch (CarNotBookedException e) {
            addErrorMessage(e.getMessage());
            return null;
        }
        addRideStartedMessage();
        return "current-order.xhtml?faces-redirect=true";
    }

    private void addRideStartedMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Enjoy.", "Your ride begins."));
    }

    private void addErrorMessage(String message){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Error.", message));
    }





    public String cancel(){
        carOrdersManager.cancelOrder(currentOrder.getId());
        return "current-order.xhtml?faces-redirect=true";
    }
}
