package org.example.biluthyrningssystem.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.biluthyrningssystem.entities.Customer;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.exceptions.UnalterableFieldException;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@Service
public class CustomerService implements CustomerServiceInterface { // Entire class made by Leo

    private final CustomerRepository customerRepository;
    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> fetchAllCustomers() {
        LOGGER.info("ADMIN fetched all customers");

        return customerRepository.findAll();
    }

    @Override
    public Customer fetchCustomerById(long id) {
        LOGGER.info("ADMIN fetched customer with ID: '{}'", id);

        return customerRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("ADMIN failed fetch, as Customer with ID: '{}' not found", id);
            return new ResourceNotFoundException("Customer", "ID not found", id);
        });
    }

    @Override
    public Customer addCustomer(Customer customer) {
        if(isValidCustomer(customer)) {
            LOGGER.info("ADMIN successfully added new customer");
            return customerRepository.save(customer);
        }
        else{
            LOGGER.error("ADMIN failed to add new customer");
            throw new IllegalArgumentException("Customer's first name, last name and social security number cannot be empty");
        }
    }

    @Override
    public Customer updateCustomer(Customer updatedCustomer, Principal principal) {
        String user = principal.getName();

        Customer existingCustomer = customerRepository.findById(updatedCustomer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", updatedCustomer.getId()));

        if(!existingCustomer.getSocialSecurityNumber().equals(user)){
            LOGGER.warn("Unauthorized USER attempted change to customer '{}'", existingCustomer.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to change this customer");
        }

        if(!existingCustomer.getSocialSecurityNumber().equals(updatedCustomer.getSocialSecurityNumber())) {
            LOGGER.error("USER tried and failed to update customer with ID: '{}'", existingCustomer.getId());
            throw new UnalterableFieldException("You cannot change the social security number from: " + existingCustomer.getSocialSecurityNumber());
        }

        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        LOGGER.info("USER successfully updated customer with ID: '{}'", existingCustomer.getId());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(long id) {

        customerRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("ADMIN tried and failed to delete customer with ID: '{}'", id);
            return new ResourceNotFoundException("Customer", "ID not found", id);
        });

        customerRepository.deleteById(id);
        LOGGER.info("ADMIN deleted Customer with ID: '{}'", id);
    }

    private boolean isValidCustomer(Customer customer) {
        return customer.getSocialSecurityNumber() != null
                && customer.getFirstName() != null
                && customer.getLastName() != null;
    }
}
