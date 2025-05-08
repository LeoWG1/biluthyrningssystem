package org.example.biluthyrningssystem.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.models.dtos.CarStatisticsDTO;
import org.example.biluthyrningssystem.models.dtos.StatisticsDTO;
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

    // Logger messages added by Leo

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
        // Check if dates are valid and availability of car
        LocalDate today = LocalDate.now();

        if (order.getStartDate().isBefore(today)) {
            ORDER_LOGGER.warn("USER attempted and failed to create new order due to unavailable start date");
            throw new IllegalArgumentException("Start date must be today or in the future");
        }

        if (order.getEndDate().isBefore(order.getStartDate())) {
            ORDER_LOGGER.warn("USER attempted and failed to create new order due to unavailable end date");
            throw new IllegalArgumentException("End date must be after start date");
        }

        Car car = carRepository.findById(order.getCar().getId())
                .orElseThrow(() -> {
                    ORDER_LOGGER.error("USER attempted to create new order with non-existent car. ID: {}", order.getCar().getId());
                    return new ResourceNotFoundException("Car", "ID not found", order.getCar().getId());
                });

        List<Order> conflictingOrders = orderRepository.findActiveOrdersWithin(
                car.getId(),
                order.getStartDate(),
                order.getEndDate()
        );

        if (!conflictingOrders.isEmpty()) {
            ORDER_LOGGER.error("USER attempted to create new order with unavailable car");
            throw new IllegalStateException("Car isn't available during the selected dates");
        }

        // Calculate total price of order
        long days = ChronoUnit.DAYS.between(order.getStartDate(), order.getEndDate());
        long totalPrice = (long) (days * car.getPricePerDay());

        // Set fields
        order.setPrice(totalPrice);
        order.setActive(true);
        order.setCar(car);

        ORDER_LOGGER.info("USER has created new order with car ID: {}", order.getCar().getId());

        return orderRepository.save(order);
    }

    @Override
    public void removeOrderById(long id) {
        orderRepository.findById(id).orElseThrow(() -> {
            ORDER_LOGGER.error("ADMIN attempted to remove non-existent order with ID: {}", id);
            return new ResourceNotFoundException("Order", "ID not found", id);
        });
        orderRepository.deleteById(id);
        ORDER_LOGGER.info("ADMIN has removed order with ID: {}", id);
    }

    @Override
    public void removeOrderBeforeDate(LocalDate date) {
        List<Order> orders = orderRepository.findByEndDateBefore(date);
        orderRepository.deleteAll(orders);
        ORDER_LOGGER.info("ADMIN has removed all orders before date: {}", date);
    }

    @Override
    public Order cancelOrder(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    ORDER_LOGGER.error("USER attempted to cancel non-existent order with ID: {}", id);
                    return new ResourceNotFoundException("Order", "id", id);
                });

        order.setActive(false);
        Car car = order.getCar();
        carRepository.save(car);

        ORDER_LOGGER.info("USER has cancelled order with ID: {}", id);

        return orderRepository.save(order);
    }

    @Override
    public StatisticsDTO getStatistics() {
        List<Order> allOrders = orderRepository.findAll();

        long totalOrders = allOrders.size();

        long totalActiveOrders = allOrders.stream()
                .filter(Order::isActive)
                .count();

        long totalRevenue = allOrders.stream()
                .mapToLong(Order::getPrice)
                .sum();

        return new StatisticsDTO(totalActiveOrders, totalOrders, totalRevenue);
    }

    @Override
    public CarStatisticsDTO getCarStatistics(long id) {
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

        return new CarStatisticsDTO(totalActiveOrders, totalOrders, totalRevenue, latestOrder);
    }
}
