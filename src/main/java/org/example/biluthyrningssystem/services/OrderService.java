package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Order;
import org.example.biluthyrningssystem.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService implements OrderServiceInterface {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public Order createOrder(Order order) {
        return null;
    }

    @Override
    public void removeOrderById(long id) {

    }

    @Override
    public void removeOrderBeforeDate(LocalDate date) {

    }
}
