package com.olrox.order;


import com.olrox.ejb.carinteraction.CarBeeper;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class BeepBean {
    @EJB
    private CarBeeper carBeeper;

    @Inject
    private CurrentOrderBean currentOrderBean;

    public String beep(){
        carBeeper.beep(currentOrderBean.getCurrentOrder().getCar());
        addBeepMessage();
        return null;
    }

    private void addBeepMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Info.", "Your car beeped."));
    }
}
