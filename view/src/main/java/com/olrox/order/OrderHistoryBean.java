package com.olrox.order;


import com.olrox.account.CurrentSessionBean;
import com.olrox.order.domain.CarOrder;
import com.olrox.order.ejb.CarOrdersManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class OrderHistoryBean {
    @EJB
    CarOrdersManager carOrdersManager;

    @Inject
    CurrentSessionBean currentSessionBean;

    List<CarOrder> orderHistory;

    @PostConstruct
    public void init(){
        orderHistory = carOrdersManager.getOrdersByUser(currentSessionBean.getCurrentUser().getId());
    }

    public List<CarOrder> getOrderHistory() {
        return orderHistory;
    }
}
