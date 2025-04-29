package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.entities.Order;
import org.example.biluthyrningssystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/activeorders") // User's active orders
    public ResponseEntity<List<Order>> getAllActiveOrders() {
        return ResponseEntity.ok(orderService.getAllActiveOrders());
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

    // WIP
    @GetMapping("/admin/statistics")
    public ResponseEntity<List<Order>> showStatistics() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
