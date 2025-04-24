package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Customer;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements CustomerServiceInterface { // Entire class made by Leo

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> fetchAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer fetchCustomerById(long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id)) ;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override // Find a solution for this
    public Customer updateCustomer(Customer customer) {

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(long id) {
        customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        customerRepository.deleteById(id);
    }
}
