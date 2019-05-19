package com.olrox.order;


import com.olrox.account.CurrentSessionBean;
import com.olrox.domain.order.CarOrder;
import com.olrox.ejb.CarOrdersManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class OrderHistoryBean {
    @EJB
    private CarOrdersManager carOrdersManager;

    @Inject
    private CurrentSessionBean currentSessionBean;

    private List<CarOrder> orderHistory;

    @PostConstruct
    public void init(){
        orderHistory = carOrdersManager.getOrdersByUser(currentSessionBean.getCurrentUser().getId());
    }

    public List<CarOrder> getOrderHistory() {
        return orderHistory;
    }
}
