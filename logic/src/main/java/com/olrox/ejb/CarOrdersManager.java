package com.olrox.ejb;

import com.olrox.domain.account.RentalUser;
import com.olrox.domain.account.Role;
import com.olrox.domain.car.Car;
import com.olrox.domain.car.CarStatus;
import com.olrox.ejb.exception.CarAlreadyBookedException;
import com.olrox.ejb.exception.CarNotBookedException;
import com.olrox.ejb.exception.IllegalRoleException;
import com.olrox.ejb.exception.TooManyActiveOrdersException;
import com.olrox.domain.order.CarOrder;
import com.olrox.domain.order.OrderStatus;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDateTime;
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

        if (rentalUser.getRole() != Role.USER) {
            throw new IllegalRoleException();
        }

        if (getActiveOrder(rentalUser.getId()) != null) {
            throw new TooManyActiveOrdersException(rentalUser.getId(), 1);
        }

        if (car.getCarStatus() == CarStatus.BOOKED) {
            throw new CarAlreadyBookedException(car.getCarNumber());
        }

        car.setCarStatus(CarStatus.BOOKED);
        entityManager.merge(car);

        CarOrder order = new CarOrder();
        order.setCar(car);
        order.setRentalUser(rentalUser);
        order.setOrderStatus(OrderStatus.BOOKING);
        order.setBookingTime(LocalDateTime.now());
        entityManager.persist(order);
        bookingTimer.startBooking(order);
        return order;
    }

    public CarOrder find(long id) {
        return entityManager.find(CarOrder.class, id);
    }

    public CarOrder getActiveOrder(long userId) throws TooManyActiveOrdersException {
        List list = entityManager
                .createQuery("from CarOrder as ord where ord.rentalUser.id=:userId" +
                        " and ord.orderStatus!='CLOSED_CANCELED' and ord.orderStatus!='CLOSED_PAID'")
                .setParameter("userId", userId).getResultList();
        int size = list.size();
        if (size > 1) {
            throw new TooManyActiveOrdersException(userId, size);
        }
        if (size == 1) {
            return (CarOrder) list.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<CarOrder> getOrdersByUser(long userId) {
        List<CarOrder> list = entityManager
                .createQuery("from CarOrder as ord where ord.rentalUser.id=:userId")
                .setParameter("userId", userId).getResultList();
        return list;
    }

    public void cancelOrder(long orderId) {
        CarOrder carOrder = this.find(orderId);
        if (carOrder.getOrderStatus() == OrderStatus.BOOKING) {
            carOrder.setOrderStatus(OrderStatus.CLOSED_CANCELED);
            Car bookedCar = carOrder.getCar();
            bookedCar.setCarStatus(CarStatus.FREE);
            entityManager.merge(bookedCar);
            entityManager.merge(carOrder);
        }
    }

    public void startRide(long orderId) throws CarNotBookedException {
        CarOrder order = this.find(orderId);
        if (order.getOrderStatus() != OrderStatus.BOOKING) {
            throw new CarNotBookedException(Long.toString(order.getCar().getId()));
        }
        order.setOrderStatus(OrderStatus.RIDE);
        order.setStartTime(LocalDateTime.now());
        entityManager.persist(order);
    }

    public void finishRide(long orderId) {
        CarOrder order = this.find(orderId);
        order.setOrderStatus(OrderStatus.RIDE_OVER);
        order.setEndTime(LocalDateTime.now());
        order.setFinalScore(calculateScore(order.getStartTime(),
                order.getEndTime(),
                order.getCar().getModel().getPricePerMinute()));
        order = entityManager.merge(order);

        Car car = order.getCar();
        car.setCarStatus(CarStatus.FREE);
        entityManager.merge(car);
    }

    private int calculateScore(LocalDateTime start, LocalDateTime finish, int pricePerMinute) {
        Duration duration = Duration.between(start, finish);
        int minutes = (int) duration.toMinutes() + 1;
        return minutes * pricePerMinute;
    }

    public void makePayment(long orderId){
        CarOrder order = find(orderId);
        order.setOrderStatus(OrderStatus.CLOSED_PAID);
        entityManager.merge(order);
    }
}
