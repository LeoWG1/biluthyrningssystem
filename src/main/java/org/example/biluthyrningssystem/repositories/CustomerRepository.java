package org.example.biluthyrningssystem.repositories;

import org.example.biluthyrningssystem.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> { // Entire class made by Leo
}
