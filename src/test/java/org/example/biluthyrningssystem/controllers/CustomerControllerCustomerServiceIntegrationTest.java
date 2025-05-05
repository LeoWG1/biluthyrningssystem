package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.entities.Customer;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.exceptions.UnalterableFieldException;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.example.biluthyrningssystem.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest()
@ExtendWith(MockitoExtension.class)
class CustomerControllerCustomerServiceIntegrationTest { // Entire class made by Leo
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerController customerController;

    private Customer customer1;
    private Customer customer2;
    private Principal principal;

    @BeforeEach
    void setUp() {
        customerController = new CustomerController(customerService);

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

        principal = new UsernamePasswordAuthenticationToken("19890101-1234", 1234);
    }

    @Test
    void getAllCustomerShouldReturnAllCustomers() {
        // Given
        List<Customer> customers = Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customers);

        // When
        ResponseEntity<List<Customer>> responseEntity = customerController.getAllCustomer();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customers, responseEntity.getBody());
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        // When
        ResponseEntity<Customer> responseEntity = customerController.getCustomerById(1L);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customer1, responseEntity.getBody());
    }

    @Test
    void getCustomerByIdShouldReturnNotFound() {
        // Given
        when(customerRepository.findById(69L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> customerController.getCustomerById(69L));
    }

    @Test
    void addCustomerShouldReturnNewCustomer() {
        // Given
        when(customerRepository.save(customer1)).thenReturn(customer1);

        // When
        ResponseEntity<Customer> responseEntity = customerController.addCustomer(customer1);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(customer1, responseEntity.getBody());
    }

    @Test
    void addCustomerShouldThrowIllegalArgumentException() {
        // Given
        Customer invalidCustomer = new Customer();

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> customerController.addCustomer(invalidCustomer));
    }

    @Test
    void deleteCustomerShouldReturnSuccessMessage() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        doNothing().when(customerRepository).deleteById(1L);

        // When
        ResponseEntity<String> responseEntity = customerController.deleteCustomer(1L);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Customer with id 1 has been deleted!", responseEntity.getBody());
    }

    @Test
    void deleteCustomerShouldThrowNotFoundException() {
        // Given
        when(customerRepository.findById(69L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> customerController.deleteCustomer(69L) );
    }

    @Test
    void updateCustomerShouldReturnUpdatedCustomer() {
        // Given
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setFirstName("John II");
        updatedCustomer.setLastName("Smithsson");
        updatedCustomer.setSocialSecurityNumber("19890101-1234");
        updatedCustomer.setEmail("john2.smithsson@gmail.com");
        updatedCustomer.setAddress("Smithgatan 3D");
        updatedCustomer.setPhoneNumber("1234567890");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(updatedCustomer));
        when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

        // When
        ResponseEntity<Customer> responseEntity = customerController.updateCustomer(updatedCustomer, principal);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCustomer, responseEntity.getBody());
    }

    @Test
    void updateCustomerShouldThrowForbiddenException() {
        // Given
        Principal unauthorizedUser = new UsernamePasswordAuthenticationToken("wrongusername", "wrongpassword");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        // When/Then
        assertThrows(ResponseStatusException.class, () -> customerController.updateCustomer(customer1, unauthorizedUser));
    }
    @Test
    void updateCustomerShouldThrowUnalterableFieldException() {
        // Given
        Customer invalidFieldUpdate = new Customer();
        invalidFieldUpdate.setId(1L);
        invalidFieldUpdate.setSocialSecurityNumber("newSocialSecurityNumber");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        // When/Then
        assertThrows(UnalterableFieldException.class, () -> customerController.updateCustomer(invalidFieldUpdate, principal));
    }
}