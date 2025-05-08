package org.example.biluthyrningssystem.controllers;

import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class CarControllerCarServiceCarRepositoryIntegrationTest {

    private CarRepository carRepository;
    private CarController carController;

    @Autowired
    public CarControllerCarServiceCarRepositoryIntegrationTest(CarRepository carRepository, CarController carController) {
        this.carRepository = carRepository;
        this.carController = carController;
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
    void addCarShouldReturnStatusCodeCreated() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        ResponseEntity<String> response = carController.addCar(car);

        assertThat(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED)).isTrue();
        assertThat(response.getBody()).isEqualTo("Car added");
    }

    @Test
    void addCarShouldReturnStatusCodeConflict() {

        Car car1 = new Car(1200.0,"BMW","520","PRE580",false);
        Car car2 = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(car1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carController.addCar(car2));
        assertThat(exception.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)).isTrue();
        assertTrue(exception.getMessage().contains("PlateNumber already exists"));
    }

    @Test
    void updateCar() {
    }

    @Test
    void removeCar() {
    }
}