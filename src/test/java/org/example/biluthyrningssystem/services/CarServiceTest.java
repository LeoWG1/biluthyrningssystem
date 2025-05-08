package org.example.biluthyrningssystem.services;

import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.models.dtos.CarDTO;
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


    @Autowired
    public CarServiceTest(CarService carService, CarRepository carRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
    }

    @Test
    void getAvailableCars() {

//        Car carNotInService = new Car(990.0, "BMW", "520", "PRE580", false);
////        Car carOnService = new Car(1100.0, "Audi", "A6", "AUD113", true);
//
//        Car savedCar = carRepository.save(carNotInService);
//        CarDTO carNotInServiceDTO = new CarDTO(savedCar);
//        List<CarDTO> cars = carService.getAvailableCars();
//
//        assertThat(cars.contains(carNotInServiceDTO)).isTrue();
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
    void addCarShouldAddCar() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        carService.addCar(car);
        List<Car> cars = carRepository.findAll();

        assertThat(cars.contains(car)).isTrue();
    }

    @Test
    void updateCarShouldUpdateCar() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);
        carService.addCar(car);
        car.setModel("525i");
        carService.updateCar(car);
        List<Car> cars = carRepository.findAll();

        assertThat(cars.contains(car)).isTrue();
    }

    @Test
    void removeCarWhenIdExistsShouldRemoveCar() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        Car carToRemove = carRepository.save(car);
        carService.removeCar(carToRemove.getId());
        List<Car> cars = carRepository.findAll();

        assertThat(cars.contains(car)).isFalse();
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