package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.dto.CarStatisticsDTO;
import org.example.biluthyrningssystem.dto.StatisticsDTO;
import org.example.biluthyrningssystem.entities.Order;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Frida Jakobsson
 */
public interface OrderServiceInterface {
    List<Order> getAllOrders();
    List<Order> getAllActiveOrders();
    List<Order> getAllOrdersByUsername(String username);
    List<Order> getAllActiveOrdersByUsername(String username);
    List<Order> getAllInactiveOrders();
    Order createOrder(Order order);
    void removeOrderById(long id);
    void removeOrderBeforeDate(LocalDate date);
    Order cancelOrder(long id);
    StatisticsDTO getStatistics();
    CarStatisticsDTO getCarStatistics(long id);
}
