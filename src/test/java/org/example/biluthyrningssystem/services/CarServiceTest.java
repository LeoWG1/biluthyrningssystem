package org.example.biluthyrningssystem.services;

import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//Made by Ann-Louis
@SpringBootTest
@Transactional
@Rollback
class CarServiceTest {

    private CarService carService;
    private CarRepository carRepository;
    private Car car;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    public CarServiceTest(CarService carService, CarRepository carRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
    }

//    @BeforeEach
//    void setUp() {
//        car.setId(1L);
//        car.setModel("BMW");
//        car.setBrand("520");
//        car.setPlateNumber("PRE580");
//        car.setPricePerDay(990.0);
//        car.setInService(true);
//    }

    @Test
    void getAvailableCars() {
    }

    @Test
    void adminGetAvailableCars() {
    }

    @Test
    void getAllCarsShouldReturnAllCars() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(car);
        List<Car> cars = carRepository.findAll();
        List<Car> foundCars = carService.getAllCars();

        assertThat(foundCars).isEqualTo(cars);
    }

    @Test
    void addCar() {
    }

    @Test
    void updateCar() {
    }

    //REMOVE CAR METHOD TESTING
    @Test
    void removeCarWhenIdExistsShouldRemoveCar() {
    }

    @Test
    void removeCarWhenIdDoesNotExistsShouldThrowException() {

    }

    @Test
    void removeCarWithActiveOrdersShouldThrowException() {
    }

    @Test
    void removeCarInServiceShouldThrowException() {

    }
}