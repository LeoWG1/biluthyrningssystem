package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.models.entities.Order;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.example.biluthyrningssystem.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//Ann-Louis made this
@ExtendWith(MockitoExtension.class)
class CarServiceUnitTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setId(1L);
        car.setBrand("BMW");
        car.setModel("520");
        car.setPricePerDay(990.0);
        car.setPlateNumber("PRE580");
        car.setInService(false);
    }

    @Test
    void getAvailableCars() {
    }

    @Test
    void getAllCarsShouldReturnAllCars() {

        when(carRepository.findAll()).thenReturn(List.of(car));

        List<Car> cars = carService.getAllCars();

        assertNotNull(cars);
        assertEquals(car, cars.get(0));
    }

    @Test
    void addCarShouldReturnCarAdded() {

        when(carRepository.save(car)).thenReturn(car);

        String result = carService.addCar(car);

        assertEquals("Car added", result);
    }

    @Test
    void updateCarWithValidDataShouldReturnCarUpdated() {
    }

    @Test
    void removeCarWhenIdExistShouldReturnCarRemoved() {
    }
}