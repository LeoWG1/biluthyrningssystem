package org.example.biluthyrningssystem.repositories;

import org.example.biluthyrningssystem.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
