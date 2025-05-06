package org.example.biluthyrningssystem.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.models.vos.CarStatisticsVO;
import org.example.biluthyrningssystem.models.vos.StatisticsVO;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.models.entities.Order;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.example.biluthyrningssystem.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Frida Jakobsson
 */
@Service
public class OrderService implements OrderServiceInterface {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private static final Logger ORDER_LOGGER = LogManager.getLogger("userlog");

    @Autowired
    public OrderService(OrderRepository orderRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        ORDER_LOGGER.info("USER fetched all orders tied to");
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(long id) { return orderRepository.findOrderById(id); }

    @Override
    public List<Order> getAllActiveOrders() {
        return orderRepository.findByActiveTrue();
    }

    @Override
    public List<Order> getAllOrdersByUsername(String username) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getCustomer().getSocialSecurityNumber().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getAllActiveOrdersByUsername(String username) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.isActive() &&
                        order.getCustomer().getSocialSecurityNumber().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getAllInactiveOrders() { return orderRepository.findByActiveFalse(); }

    @Override
    @Transactional // Cancels entire process if anything goes wrong
    public Order createOrder(Order order) {
        // Check if car exists
        Car car = carRepository.findById(order.getCar().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", order.getCar().getId()));

        // Check if car is available for the dates
        List<Order> conflictingOrders = orderRepository.findActiveOrdersWithin(
                car.getId(),
                order.getStartDate(),
                order.getEndDate()
        );

        if (!conflictingOrders.isEmpty()) {
            throw new IllegalStateException("Car isn't available during the selected dates");
        }

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
        carRepository.save(car);

        return orderRepository.save(order);
    }

    @Override
    public StatisticsVO getStatistics() {
        List<Order> allOrders = orderRepository.findAll();

        long totalOrders = allOrders.size();

        long totalActiveOrders = allOrders.stream()
                .filter(Order::isActive)
                .count();

        long totalRevenue = allOrders.stream()
                .mapToLong(Order::getPrice)
                .sum();

        return new StatisticsVO(totalActiveOrders, totalOrders, totalRevenue);
    }

    @Override
    public CarStatisticsVO getCarStatistics(long id) {
        List<Order> relevantOrders = orderRepository.findByCarId(id);

        long totalOrders = relevantOrders.size();

        long totalActiveOrders = relevantOrders.stream()
                .filter(Order::isActive)
                .count();

        long totalRevenue = relevantOrders.stream()
                .mapToLong(Order::getPrice)
                .sum();

        LocalDate latestOrder = relevantOrders.stream()
                .map(Order::getEndDate)
                .max(Comparator.naturalOrder())
                .orElse(null);

        return new CarStatisticsVO(totalActiveOrders, totalOrders, totalRevenue, latestOrder);
    }
}
