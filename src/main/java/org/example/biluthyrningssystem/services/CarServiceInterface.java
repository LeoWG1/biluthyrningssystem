package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.dto.CarDTO;
import org.example.biluthyrningssystem.entities.Car;

import java.util.List;

// Ann-Louis made this class
public interface CarServiceInterface {

    List<CarDTO> getAvailableCars();
    List<Car> adminGetAvailableCars();
    List<Car> getAllCars();
    String addCar(Car car);
    String updateCar(Car car);
    String removeCar(long id);
}
