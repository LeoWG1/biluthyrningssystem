package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.models.dtos.CarStatisticsDTO;
import org.example.biluthyrningssystem.models.dtos.StatisticsDTO;
import org.example.biluthyrningssystem.models.entities.Customer;
import org.example.biluthyrningssystem.models.entities.Order;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.example.biluthyrningssystem.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Frida Jakobsson
 */
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderController(OrderService orderService, CustomerRepository customerRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
    }

    /*
    USER ENDPOINTS
     */
    @GetMapping("/orders") // All of user's orders
    public ResponseEntity<List<Order>> getAllOrders(Principal principal) {
        List<Order> userOrders = orderService.getAllOrdersByUsername(principal.getName());
        return ResponseEntity.ok(userOrders);
    }

    @GetMapping("/activeorders") // User's active orders
    public ResponseEntity<List<Order>> getAllActiveOrders(Principal principal) {
        List<Order> activeUserOrders = orderService.getAllActiveOrdersByUsername(principal.getName());
        return ResponseEntity.ok(activeUserOrders);
    }

    @PostMapping("/addorder")
    public ResponseEntity<?> addOrder(@RequestBody Order order, Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findCustomerBySocialSecurityNumber(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Username", username));

        order.setCustomer(customer);
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }

    @PutMapping("/cancelorder/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") long id, Principal principal) {
        Order order = orderService.getOrderById(id);

        if (!(principal.getName().equals(order.getCustomer().getSocialSecurityNumber()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Unable to remove orders belonging to other users");
        }
        Order canceledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(canceledOrder);
    }

    /*
    ADMIN ENDPOINTS
     */
    @GetMapping("/admin/orders") // All historical (inactive) orders
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
