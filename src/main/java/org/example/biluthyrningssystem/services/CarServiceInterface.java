package org.example.biluthyrningssystem.services;

import org.apache.coyote.Response;
import org.example.biluthyrningssystem.CarDTO;
import org.example.biluthyrningssystem.entities.Car;
import org.springframework.http.ResponseEntity;

import java.util.List;

// Ann-Louis made this class
public interface CarServiceInterface {

    List<CarDTO> getAvailableCars();
    List<Car> adminGetAvailableCars();
    List<Car> getAllCars();
    String addCar(Car car);
    String updateCar(Car car);
    String removeCar(Long id);
}
