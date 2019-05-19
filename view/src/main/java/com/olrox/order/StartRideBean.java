package com.olrox.order;

import com.olrox.ejb.exception.CarNotBookedException;
import com.olrox.ejb.CarOrdersManager;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class StartRideBean {
    @EJB
    private CarOrdersManager carOrdersManager;

    @Inject
    CurrentOrderBean currentOrderBean;

    public String startRide(){
        try {
            carOrdersManager.startRide(currentOrderBean.getCurrentOrder().getId());
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
}
