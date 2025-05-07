package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.models.dtos.CarDTO;
import org.example.biluthyrningssystem.models.entities.Car;

import java.util.List;

// Ann-Louis made this class
public interface CarServiceInterface {

    List<CarDTO> getAvailableCars();
    List<Car> getAllCars();
    Car addCar(Car car);
    String updateCar(Car car);
    String removeCar(long id);
}
