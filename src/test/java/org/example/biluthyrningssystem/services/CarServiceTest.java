package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarServiceTest {

    private CarService carService;
    private CarRepository carRepository;

    @Autowired
    public CarServiceTest(CarService carService, CarRepository carRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
    }

    @Test
    void getAvailableCars() {
    }

    @Test
    void adminGetAvailableCars() {
    }

    @Test
    void getAllCars() {
    }

    @Test
    void addCar() {
    }

    @Test
    void updateCar() {
    }

    @Test
    void removeCar() {
    }
}