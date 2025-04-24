package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Car;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService implements CarServiceInterface {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public ResponseEntity<List<Car>> getCars() {
        return ResponseEntity.ok(carRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carRepository.findAll());
    }


    @Override
    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public String updateCar(Car car) {
        return "";
    }

    @Override
    public String removeCar(Car car) {
        carRepository.delete(car);
        return "Car deleted";
    }
}
