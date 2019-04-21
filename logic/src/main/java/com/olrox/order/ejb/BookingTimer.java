package com.olrox.order.ejb;

import com.olrox.account.domain.RentalUser;
import com.olrox.car.domain.Car;
import com.olrox.car.ejb.CarsManager;
import com.olrox.order.domain.CarOrder;
import com.olrox.order.domain.Status;

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
    public final static long DURATION = 20000;

    @EJB
    CarsManager carsManager;

    @EJB
    CarOrdersManager carOrdersManager;

    @Resource
    protected TimerService timerService;

    @Timeout
    public void timeoutHandler(Timer timer) {
        long id = (long)timer.getInfo();
        CarOrder carOrder = carOrdersManager.find(id);
        if(carOrder.getStatus() == Status.BOOKED) {
            carOrder.setStatus(Status.CLOSED);
            Car bookedCar = carOrder.getCar();
            bookedCar.setStatus(com.olrox.car.domain.Status.FREE);
            carsManager.merge(bookedCar);
            carOrdersManager.merge(carOrder);
        }
    }

    public void startBooking(CarOrder carOrder){
        TimerConfig config = new TimerConfig();
        config.setInfo(carOrder.getId());
        timerService.createSingleActionTimer(DURATION, config);
    }
}
