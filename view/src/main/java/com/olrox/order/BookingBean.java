package com.olrox.order;

import com.olrox.account.CurrentSessionBean;
import com.olrox.domain.account.RentalUser;
import com.olrox.domain.car.Car;
import com.olrox.ejb.exception.CarAlreadyBookedException;
import com.olrox.ejb.exception.TooManyActiveOrdersException;
import com.olrox.ejb.exception.IllegalRoleException;
import com.olrox.map.CarSelectBean;
import com.olrox.ejb.CarOrdersManager;
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
    private CarOrdersManager carOrdersManager;

    @Inject
    private CarSelectBean carSelectBean;

    @Inject
    private CurrentSessionBean currentSessionBean;

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
            } catch (TooManyActiveOrdersException e) {
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
