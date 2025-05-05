package org.example.biluthyrningssystem.repositories;


import org.example.biluthyrningssystem.entities.Car;
import org.example.biluthyrningssystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Ann-Louis made this class
@Repository
public interface  CarRepository extends JpaRepository<Car, Long> {

    Car getCarById(Long id);

}
