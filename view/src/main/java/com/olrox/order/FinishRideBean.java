package com.olrox.order;

import com.olrox.ejb.carinteraction.CarUpdater;
import com.olrox.ejb.CarOrdersManager;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class FinishRideBean {
    @EJB
    private CarOrdersManager carOrdersManager;

    @EJB
    private CarUpdater carUpdater;

    @Inject
    private CurrentOrderBean currentOrderBean;

    public String finishRide(){
        carOrdersManager.finishRide(currentOrderBean.getCurrentOrder().getId());
        carUpdater.updateCarInfo(currentOrderBean.getCurrentCar().getId());
        return "current-order.xhtml?faces-redirect=true";
    }
}
