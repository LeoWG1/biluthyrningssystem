package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Customer;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.exceptions.UnalterableFieldException;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest { // Entire class by Leo
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer1;
    private Customer customer2;

    // Behövs testning för isValidCustomer()? Fråga Michael

    @BeforeEach
    void setUp(){
        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("John");
        customer1.setLastName("Smith");
        customer1.setSocialSecurityNumber("19890101-1234");
        customer1.setEmail("john.smith@gmail.com");
        customer1.setAddress("Smithgatan 3D");
        customer1.setPhoneNumber("1234567890");

        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setSocialSecurityNumber("19890102-1234");
        customer2.setEmail("jane.smith@gmail.com");
        customer2.setAddress("Smithgatan 3D");
        customer2.setPhoneNumber("1234567890");
    }

    @Test
    void fetchAllCustomers_ShouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> customers = customerService.fetchAllCustomers();

        assertNotNull(customers);
        verify(customerRepository).findAll();
    }

    @Test
    void fetchCustomerById_WhenIdExists_ShouldReturnCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        Customer customerReturned = customerService.fetchCustomerById(1L);

        assertNotNull(customerReturned);
        verify(customerRepository).findById(1L);
    }

    @Test
    void fetchCustomerById_WhenIDNotExists_ShouldThrowNotFoundException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.fetchCustomerById(99L);
        });

        verify(customerRepository).findById(99L);
    }

    @Test
    void addCustomer_WhenValidData_ShouldAddCustomer() {
        when(customerRepository.save(customer1)).thenReturn(customer1);

        Customer newCustomer = customerService.addCustomer(customer1);

        assertNotNull(newCustomer);
        verify(customerRepository).save(customer1);
    }

    @Test
    void addCustomer_WhenInvalidData_ShouldThrowIllegalArgumentException() {
        Customer invalidCustomer = new Customer();

        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(invalidCustomer));

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_WhenAuthorized_ShouldUpdateCustomer() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("19890101-1234");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setFirstName("John");
        updatedCustomer.setSocialSecurityNumber("19890101-1234");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(updatedCustomer, principal);

        assertEquals(updatedCustomer.getFirstName(), result.getFirstName());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_WhenUnauthorized_ShouldThrowResponseStatusException() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("wrongUsername");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        assertThrows(ResponseStatusException.class, () -> {
            customerService.updateCustomer(customer1, principal);
        });

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_WhenAuthorizedButSsnChanged_ShouldThrowUnalterableFieldException() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("19890101-1234");

        Customer invalidUpdate = new Customer();
        invalidUpdate.setId(1L);
        invalidUpdate.setSocialSecurityNumber("newSocialSecurityNumber");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        assertThrows(UnalterableFieldException.class, () -> {
            customerService.updateCustomer(invalidUpdate, principal);
        });

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_WhenIdExists_ShouldDeleteCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        customerService.deleteCustomer(1L);

        verify(customerRepository).deleteById(1L);

    }

    @Test
    void deleteCustomer_WhenIdNotExists_ShouldThrowNotFoundException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.deleteCustomer(99L);
        });

        verify(customerRepository, never()).deleteById(99L);
    }


}