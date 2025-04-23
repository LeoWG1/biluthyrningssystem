package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Car;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CarServiceInterface {

    ResponseEntity<List<Car>> getCars();
    ResponseEntity<List<Car>> getAllCars();
    ResponseEntity<Car> addCar(Car car);
    ResponseEntity<Car> updateCar(Car car);
    ResponseEntity<Car> removeCar(Car car);

}
