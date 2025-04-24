package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Customer;

import java.util.List;

public interface CustomerServiceInterface { // Entire class made by Leo

    List<Customer> fetchAllCustomers();
    Customer fetchCustomerById(long id);
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(long id);
}
