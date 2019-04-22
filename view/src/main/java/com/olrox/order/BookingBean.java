package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.account.domain.RentalUser;
import com.olrox.account.ejb.RentalUsersManager;
import com.olrox.car.domain.Car;
import com.olrox.exception.CarAlreadyBookedException;
import com.olrox.exception.HavingUnclosedOrdersException;
import com.olrox.exception.IllegalRoleException;
import com.olrox.map.CarSelectBean;
import com.olrox.order.ejb.CarOrdersManager;
import org.primefaces.PrimeFaces;

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
    private final static String DLG_VAR = "bookedDlg";

    @EJB
    CarOrdersManager carOrdersManager;

    @EJB
    RentalUsersManager rentalUsersManager;

    @Inject
    CarSelectBean carSelectBean;

    @Inject
    CurrentSessionBean currentSessionBean;

    public void book(){
        Car car = carSelectBean.getCar();

        if(currentSessionBean.getRole() == null){
            redirectToAuthorizationPage();
        }
        else {
            try {
                RentalUser rentalUser = currentSessionBean.getCurrentUser();
                carOrdersManager.createBookingOrder(car, rentalUser);
            } catch (IllegalRoleException e) {
                addIllegalRoleMessage();
                return;
            } catch (CarAlreadyBookedException e) {
                addAlreadyBookedMessage(e.getMessage());
                return;
            } catch (HavingUnclosedOrdersException e) {
                addUnclosedOrdersMessage();
                return;
            }
            PrimeFaces.current().executeScript("PF('"+ DLG_VAR +"').show();");
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

    public void addIllegalRoleMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Sorry.", "You are not the user."));
    }

    public String getDlgVar() {
        return DLG_VAR;
    }
}
