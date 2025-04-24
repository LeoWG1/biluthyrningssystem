package org.example.biluthyrningssystem.services;

import org.apache.coyote.Response;
import org.example.biluthyrningssystem.entities.Car;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CarServiceInterface {

    ResponseEntity<List<Car>> getCars();
    ResponseEntity<List<Car>> getAllCars();
    Car addCar(Car car);
    String updateCar(Car car);
    String removeCar(Car car);
}
