package org.example.biluthyrningssystem.services;

import org.apache.coyote.Response;
import org.example.biluthyrningssystem.entities.Car;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CarServiceInterface {

    List<Car> getAvailableCars();
    List<Car> adminGetAvailableCars();
    List<Car> getAllCars();
    String addCar(Car car);
    String updateCar(Car car);
    String removeCar(Long id);
}
