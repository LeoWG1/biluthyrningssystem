package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Order;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService implements OrderServiceInterface {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void removeOrderById(long id) {
        orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "Id", id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Order", "Id", id));
        order.setActive(false);
        return orderRepository.save(order);
    }
}
