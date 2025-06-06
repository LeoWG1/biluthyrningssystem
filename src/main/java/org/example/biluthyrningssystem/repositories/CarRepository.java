package org.example.biluthyrningssystem.repositories;


import org.example.biluthyrningssystem.models.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Ann-Louis made this class
@Repository
public interface  CarRepository extends JpaRepository<Car, Long> {

    Car getCarById(long id);
}
