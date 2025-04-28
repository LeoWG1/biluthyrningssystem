package org.example.biluthyrningssystem.services;

import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.entities.Car;
import org.example.biluthyrningssystem.entities.Order;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.example.biluthyrningssystem.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author Frida Jakobsson
 */
@Service
public class OrderService implements OrderServiceInterface {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllActiveOrders() {
        return orderRepository.findByActiveTrue();
    }

    @Override
    public List<Order> getAllInactiveOrders() { return orderRepository.findByActiveFalse(); }

    @Override
    @Transactional // Cancels entire process if anything goes wrong
    public Order createOrder(Order order) {
        // Set car's status as booked if it's available
        Car car = carRepository.findById(order.getCar().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", order.getCar().getId()));

        if (car.isBooked()) {
            throw new IllegalStateException("Car is already booked");
        }

        car.setBooked(true);

        // Calculate total price of order
        long days = ChronoUnit.DAYS.between(order.getStartDate(), order.getEndDate());
        long totalPrice = (long) (days * car.getPricePerDay());

        // Set fields
        order.setPrice(totalPrice);
        order.setActive(true);
        order.setCar(car);

        return orderRepository.save(order);
    }

    @Override
    public void removeOrderById(long id) {
        orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        orderRepository.deleteById(id);
    }

    @Override
    public void removeOrderBeforeDate(LocalDate date) {
        List<Order> orders = orderRepository.findByEndDateBefore(date);
        orderRepository.deleteAll(orders);
    }

    @Override
    public Order cancelOrder(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        order.setActive(false);
        Car car = order.getCar();
        car.setBooked(false);
        carRepository.save(car);

        return orderRepository.save(order);
    }
}
