package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.entities.Customer;
import org.example.biluthyrningssystem.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class CustomerController { // Entire class made by Leo

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/admin/customers")
    public ResponseEntity<List<Customer>> getAllCustomer() {
        return ResponseEntity.ok(customerService.fetchAllCustomers());
    }

    @GetMapping("/admin/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {
        return ResponseEntity.ok(customerService.fetchCustomerById(id));
    }

    @PostMapping("/admin/addcustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/removecustomer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Customer with id " + id + " has been deleted!", HttpStatus.OK);
    }

    @PutMapping("/updateinfo")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, Principal principal) {
        return ResponseEntity.ok(customerService.updateCustomer(customer, principal));
    }
}
