package com.olrox.ejb;

import com.olrox.domain.order.CarOrder;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Stateless
@LocalBean
public class BookingTimer {
    public final static long DURATION = 30000;

    @EJB
    private CarOrdersManager carOrdersManager;

    @Resource
    protected TimerService timerService;

    @Timeout
    public void timeoutHandler(Timer timer) {
        long id = (long)timer.getInfo();
        carOrdersManager.cancelOrder(id);
    }

    public void startBooking(CarOrder carOrder){
        TimerConfig config = new TimerConfig();
        config.setInfo(carOrder.getId());
        timerService.createSingleActionTimer(DURATION, config);
    }
}
