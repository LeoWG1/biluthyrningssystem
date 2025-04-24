package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.entities.Customer;
import org.example.biluthyrningssystem.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("api/v1/admin/customers")
    public ResponseEntity<List<Customer>> getAllCustomer() {
        return ResponseEntity.ok(customerService.fetchAllCustomers());
    }

    @GetMapping("api/v1/admin/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {
        return ResponseEntity.ok(customerService.fetchCustomerById(id));
    }

    @PostMapping("api/v1/admin/addcustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.CREATED);
    }
    @DeleteMapping("api/v1/admin/removecustomer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Customer with id " + id + " has been deleted!", HttpStatus.OK);
    }

    @PutMapping("api/v1/updateinfo")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer Customer) {
        return ResponseEntity.ok(customerService.updateCustomer(Customer));
    }

}
