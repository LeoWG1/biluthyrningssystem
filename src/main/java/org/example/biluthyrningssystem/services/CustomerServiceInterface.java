package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.models.entities.Customer;

import java.security.Principal;
import java.util.List;

public interface CustomerServiceInterface { // Entire class made by Leo

    List<Customer> fetchAllCustomers();
    Customer fetchCustomerById(long id);
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Customer customer, Principal principal);
    void deleteCustomer(long id);
}
