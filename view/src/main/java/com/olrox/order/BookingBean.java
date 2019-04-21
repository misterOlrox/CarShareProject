package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.car.domain.Car;
import com.olrox.exception.CarAlreadyBookedException;
import com.olrox.exception.HavingUnclosedOrdersException;
import com.olrox.exception.IllegalRoleException;
import com.olrox.map.CarSelectBean;
import com.olrox.order.ejb.CarOrdersManager;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
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
        String login = currentSessionBean.getLogin();

        if(currentSessionBean.getRole() == null){
            redirectToAuthorizationPage();
        }
        else {

            try {
                carOrdersManager.createBookingOrder(car, login);
            } catch (IllegalRoleException e) {
                e.printStackTrace();
                return;
            } catch (CarAlreadyBookedException e) {
                addAlreadyBookedMessage(e.getMessage());
                return;
            } catch (HavingUnclosedOrdersException e) {
                addUnclosedOrdersMessage();
                return;
            }
            addSuccessMessage();
        }
    }

    private void redirectToAuthorizationPage(){
        currentSessionBean.setRequestedPage("map.xhtml");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSuccessMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Success!", "Car " + carSelectBean.getCar().getCarNumber() + " booked."));
    }

    private void addAlreadyBookedMessage(String message){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Sorry.", message));
    }

    public void addUnclosedOrdersMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Sorry.", "You have unclosed orders."));
    }
}
