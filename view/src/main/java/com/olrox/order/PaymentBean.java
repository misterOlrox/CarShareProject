package com.olrox.order;

import com.olrox.order.ejb.CarOrdersManager;
import org.primefaces.PrimeFaces;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class PaymentBean {
    public final static String DLG_VAR = "paymentDlg";

    @Inject
    private CurrentOrderBean currentOrderBean;

    @EJB
    private CarOrdersManager carOrdersManager;

    public String getDlgVar() {
        return DLG_VAR;
    }

    public void openPaymentDialog(){
        PrimeFaces.current().executeScript("PF('"+ PaymentBean.DLG_VAR +"').show();");
    }

    public String pay(){
        long orderId = currentOrderBean.getCurrentOrder().getId();
        carOrdersManager.makePayment(orderId);
        return "current-order.xhtml?faces-redirect=true";
    }
}
