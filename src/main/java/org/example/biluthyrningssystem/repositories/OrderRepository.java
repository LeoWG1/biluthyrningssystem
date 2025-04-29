package org.example.biluthyrningssystem.repositories;

import org.example.biluthyrningssystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Frida Jakobsson
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.car.id = :carId AND o.active = true AND " +
            "(o.startDate <= :endDate AND o.endDate >= :startDate)")
    List<Order> findActiveOrdersWithin(
            @Param("carId") long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    List<Order> findByEndDateBefore(LocalDate endDate);
    List<Order> findByActiveTrue();
    List<Order> findByActiveFalse();
}
