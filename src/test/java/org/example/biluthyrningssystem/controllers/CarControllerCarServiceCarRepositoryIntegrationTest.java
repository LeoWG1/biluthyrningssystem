package org.example.biluthyrningssystem.controllers;

import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.models.dtos.CarDTO;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//Ann-Louis made this
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
    void getAvailableCarsShouldReturnStatusCodeOK() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);
        car.setOrders(null);

        carRepository.save(car);
        ResponseEntity<List<CarDTO>> response = carController.getAvailableCars();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void adminGetAvailableCarsShouldReturnStatusCodeOK() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);
        car.setOrders(null);

        carRepository.save(car);
        ResponseEntity<List<CarDTO>> response = carController.adminGetAvailableCars();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void getAllCarsShouldReturnStatusCodeOK() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(car);
        ResponseEntity<List<Car>> response = carController.getAllCars();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
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
    void updateCarShouldReturnStatusCodeOk() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(car);
        ResponseEntity<String> response = carController.updateCar(car);

        assertThat(response.getStatusCode().isSameCodeAs(HttpStatus.OK)).isTrue();
        assertThat(response.getBody()).isEqualTo("Car updated");
    }

    @Test
    void updateCarShouldReturnStatusCodeBadRequest() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(car);
        car.setPricePerDay(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carController.updateCar(car));
        assertThat(exception.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)).isTrue();
        assertTrue(exception.getMessage().contains("Missing some data"));
    }

    @Test
    void removeCarShouldReturnStatusCodeBadRequest() {

        Car car = new Car(990.0,"BMW","520","PRE580",true);

        carRepository.save(car);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carController.removeCar(car.getId()));
        assertThat(exception.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)).isTrue();
        assertTrue(exception.getMessage().contains("Car is in service and can not be removed"));
    }
}