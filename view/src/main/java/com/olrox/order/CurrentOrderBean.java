package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.account.domain.RentalUser;
import com.olrox.car.domain.Car;
import com.olrox.exception.TooManyActiveOrdersException;
import com.olrox.order.domain.CarOrder;
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

    @Inject
    CurrentSessionBean currentSessionBean;

    CarOrder currentOrder;

    public CarOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(CarOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Car getCurrentCar(){
        if(isActive()){
            return currentOrder.getCar();
        }
        else{
            return null;
        }
    }

    public boolean isActive(){
        if(currentOrder==null){
            return false;
        }
        else {
            return true;
        }
    }

    @PostConstruct
    public void update(){
        RentalUser user = currentSessionBean.getCurrentUser();
        try {
            currentOrder = carOrdersManager.getActiveOrder(user.getId());
        } catch (TooManyActiveOrdersException e) {
            addFatalMessage(e.getMessage());
            return;
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
