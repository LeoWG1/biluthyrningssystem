package org.example.biluthyrningssystem.repositories;

import org.example.biluthyrningssystem.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> { // Entire class made by Leo
    Optional<Customer> findCustomerBySocialSecurityNumber(String socialSecurityNumber);
}
