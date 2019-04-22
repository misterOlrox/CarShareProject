package com.olrox.order.ejb;

import com.olrox.account.domain.RentalUser;
import com.olrox.account.domain.Role;
import com.olrox.car.domain.Car;
import com.olrox.car.domain.CarStatus;
import com.olrox.exception.CarAlreadyBookedException;
import com.olrox.exception.HavingUnclosedOrdersException;
import com.olrox.exception.IllegalRoleException;
import com.olrox.order.domain.CarOrder;
import com.olrox.order.domain.OrderStatus;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@LocalBean
public class CarOrdersManager {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    @EJB
    private BookingTimer bookingTimer;

    public CarOrder createBookingOrder(Car car, RentalUser rentalUser) throws    IllegalRoleException,
                                                                        CarAlreadyBookedException,
                                                                        HavingUnclosedOrdersException {
        if(rentalUser.getRole() != Role.USER){
            throw new IllegalRoleException();
        }

        if(getUserUnclosedOrders(rentalUser.getId()).size()>0){
            throw new HavingUnclosedOrdersException(Long.toString(rentalUser.getId()));
        }

        if(car.getCarStatus() == CarStatus.BOOKED){
            throw new CarAlreadyBookedException(car.getCarNumber());
        }


        car.setCarStatus(CarStatus.BOOKED);
        entityManager.merge(car);

        CarOrder order = new CarOrder();
        order.setCar(car);
        order.setRentalUser(rentalUser);
        order.setOrderStatus(OrderStatus.BOOKED);
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

    public List getUserUnclosedOrders(long userId){
        return entityManager
                .createQuery("from CarOrder as ord where ord.rentalUser.id=:userId and ord.orderStatus!='CLOSED'")
                .setParameter("userId", userId).getResultList();
    }
}
