package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.models.dtos.CarStatisticsDTO;
import org.example.biluthyrningssystem.models.dtos.StatisticsDTO;
import org.example.biluthyrningssystem.models.entities.Order;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Frida Jakobsson
 */
public interface OrderServiceInterface {
    List<Order> getAllOrders();
    Order getOrderById(long id);
    List<Order> getAllActiveOrders();
    List<Order> getAllInactiveOrders();
    List<Order> getAllOrdersByUsername(String username);
    List<Order> getAllActiveOrdersByUsername(String username);
    Order createOrder(Order order);
    void removeOrderById(long id);
    void removeOrderBeforeDate(LocalDate date);
    Order cancelOrder(long id);
    StatisticsDTO getStatistics();
    CarStatisticsDTO getCarStatistics(long id);
}
