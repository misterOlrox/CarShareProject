package com.olrox.order.ejb;

import com.olrox.car.domain.Car;
import com.olrox.car.domain.CarStatus;
import com.olrox.car.ejb.CarsManager;
import com.olrox.order.domain.CarOrder;
import com.olrox.order.domain.OrderStatus;

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
    CarOrdersManager carOrdersManager;

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
