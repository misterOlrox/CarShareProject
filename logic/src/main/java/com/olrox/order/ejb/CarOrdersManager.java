package com.olrox.order.ejb;

import com.olrox.account.domain.RentalUser;
import com.olrox.account.domain.Role;
import com.olrox.car.domain.Car;
import com.olrox.car.domain.CarStatus;
import com.olrox.exception.CarAlreadyBookedException;
import com.olrox.exception.TooManyActiveOrdersException;
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

    public CarOrder createBookingOrder(Car car, RentalUser rentalUser) throws
            IllegalRoleException, CarAlreadyBookedException, TooManyActiveOrdersException {

        if(rentalUser.getRole() != Role.USER){
            throw new IllegalRoleException();
        }

        if(getActiveOrder(rentalUser.getId())!=null){
            throw new TooManyActiveOrdersException(rentalUser.getId(), 1);
        }

        if(car.getCarStatus() == CarStatus.BOOKED){
            throw new CarAlreadyBookedException(car.getCarNumber());
        }

        car.setCarStatus(CarStatus.BOOKED);
        entityManager.merge(car);

        CarOrder order = new CarOrder();
        order.setCar(car);
        order.setRentalUser(rentalUser);
        order.setOrderStatus(OrderStatus.BOOKING);
        entityManager.persist(order);
        bookingTimer.startBooking(order);
        return order;
    }

    public CarOrder find(long id){
        return entityManager.find(CarOrder.class, id);
    }

    public CarOrder getActiveOrder(long userId) throws TooManyActiveOrdersException {
        List list = entityManager
                .createQuery("from CarOrder as ord where ord.rentalUser.id=:userId" +
                        " and ord.orderStatus!='CLOSED_CANCELED' and ord.orderStatus!='CLOSED_PAID'")
                .setParameter("userId", userId).getResultList();
        int size = list.size();
        if(size>1){
            throw new TooManyActiveOrdersException(userId, size);
        }
        if(size==1){
            return (CarOrder)list.get(0);
        }
        else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<CarOrder> getOrdersByUser(long userId){
        List<CarOrder> list = entityManager
                .createQuery("from CarOrder as ord where ord.rentalUser.id=:userId")
                .setParameter("userId", userId).getResultList();
        return list;
    }

    public void cancelOrder(long orderId){
        CarOrder carOrder = this.find(orderId);
        if(carOrder.getOrderStatus() == OrderStatus.BOOKING) {
            carOrder.setOrderStatus(OrderStatus.CLOSED_CANCELED);
            Car bookedCar = carOrder.getCar();
            bookedCar.setCarStatus(CarStatus.FREE);
            entityManager.merge(bookedCar);
            entityManager.merge(carOrder);
        }
    }
}
