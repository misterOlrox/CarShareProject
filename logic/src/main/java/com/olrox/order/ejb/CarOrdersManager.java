package com.olrox.order.ejb;

import com.olrox.account.domain.Credentials;
import com.olrox.account.domain.RentalUser;
import com.olrox.account.domain.Role;
import com.olrox.car.domain.Car;
import com.olrox.exception.IllegalRoleException;
import com.olrox.order.domain.CarOrder;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class CarOrdersManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    @EJB
    private BookingTimer bookingTimer;

    public CarOrder createBookingOrder(Car car, String login) throws IllegalRoleException {
        RentalUser rentalUser = entityManager.find(Credentials.class, login).getRentalUser();
        if(rentalUser.getRole() != Role.USER){
            throw new IllegalRoleException(login);
        }

        car.setStatus(com.olrox.car.domain.Status.BOOKED);
        entityManager.merge(car);

        CarOrder order = new CarOrder();
        order.setCar(car);
        order.setRentalUser(rentalUser);
        order.setStatus(com.olrox.order.domain.Status.BOOKED);
        entityManager.persist(order);
        bookingTimer.startBooking(order);
        return order;
    }

    public CarOrder find(long id){
        return entityManager.find(CarOrder.class, id);
    }

    public void merge(CarOrder carOrder){
        entityManager.merge(carOrder);
    }
}
