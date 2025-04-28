package org.example.biluthyrningssystem.repositories;

import org.example.biluthyrningssystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByEndDateBefore(LocalDate endDate);
    List<Order> findByActiveTrue();
}
