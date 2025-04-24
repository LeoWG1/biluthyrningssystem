package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderServiceInterface {
    List<Order> getAllOrders();
    Order createOrder(Order order);
    void removeOrderById(long id);
    void removeOrderBeforeDate(LocalDate date);
}
