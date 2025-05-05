package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.dto.CarStatisticsDTO;
import org.example.biluthyrningssystem.dto.StatisticsDTO;
import org.example.biluthyrningssystem.entities.Order;
import org.example.biluthyrningssystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Frida Jakobsson
 */
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /*
    USER ENDPOINTS
     */
    @GetMapping("/orders") // All of user's orders
    public ResponseEntity<List<Order>> getAllOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        List<Order> userOrders = orderService.getAllOrdersByUsername(loggedInUsername);
        return ResponseEntity.ok(userOrders);
    }

    @GetMapping("/activeorders") // User's active orders
    public ResponseEntity<List<Order>> getAllActiveOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        List<Order> activeUserOrders = orderService.getAllActiveOrdersByUsername(loggedInUsername);
        return ResponseEntity.ok(activeUserOrders);
    }

    @PostMapping("/addorder")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }

    @PutMapping("/cancelorder/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable("id") long id) {
        Order canceledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(canceledOrder);
    }

    /*
    ADMIN ENDPOINTS
     */
    @GetMapping("/admin/orders")
    public ResponseEntity<List<Order>> getAllOrdersAdmin() {
        return ResponseEntity.ok(orderService.getAllInactiveOrders());
    }

    @DeleteMapping("/admin/removeorder/{id}")
    public ResponseEntity<String> removeOrderByID(@PathVariable("id") long id) {
        orderService.removeOrderById(id);
        return new ResponseEntity<>("Order with id " + id + " deleted", HttpStatus.OK);
    }

    @DeleteMapping("/admin/removeorders-beforedate/{date}")
    public ResponseEntity<String> removeOrderBeforeDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        orderService.removeOrderBeforeDate(date);
        return ResponseEntity.ok("All orders dated before " + date + " were removed");
    }

    @GetMapping("/admin/activeorders") // All active orders
    public ResponseEntity<List<Order>> getAllActiveOrdersAdmin() {
        return ResponseEntity.ok(orderService.getAllActiveOrders());
    }

    @GetMapping("/admin/statistics")
    public ResponseEntity<StatisticsDTO> showStatistics() {
        return ResponseEntity.ok(orderService.getStatistics());
    }

    @GetMapping("/admin/statistics/{id}")
    public ResponseEntity<CarStatisticsDTO> showCarStatistics(@PathVariable ("id") long id) {
        return ResponseEntity.ok(orderService.getCarStatistics(id));
    }
}
