package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Customer;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.exceptions.UnalterableFieldException;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements CustomerServiceInterface { // Entire class made by Leo

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

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

    @Override // Needs to ignore changes to SSN and ID, and maybe even notify user
    public Customer updateCustomer(Customer updatedCustomer) {

        Customer existingCustomer = customerRepository.findById(updatedCustomer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", updatedCustomer.getId()));

        if(!existingCustomer.getSocialSecurityNumber().equals(updatedCustomer.getSocialSecurityNumber())) {
            throw new UnalterableFieldException("You cannot change the social security number from: " + existingCustomer.getSocialSecurityNumber());
        }

        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        return customerRepository.save(updatedCustomer);
    }

    @Override
    public void deleteCustomer(long id) {
        customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        customerRepository.deleteById(id);
    }
}
