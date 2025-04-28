package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Order;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Frida Jakobsson
 */
public interface OrderServiceInterface {
    List<Order> getAllOrders();
    List<Order> getAllActiveOrders();
    List<Order> getAllInactiveOrders();
    Order createOrder(Order order);
    void removeOrderById(long id);
    void removeOrderBeforeDate(LocalDate date);
    Order cancelOrder(long id);
}
