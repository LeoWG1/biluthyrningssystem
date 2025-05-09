package org.example.biluthyrningssystem.services;

import jakarta.transaction.Transactional;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.models.dtos.CarDTO;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.models.entities.Customer;
import org.example.biluthyrningssystem.models.entities.Order;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.example.biluthyrningssystem.repositories.CustomerRepository;
import org.example.biluthyrningssystem.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//Made by Ann-Louis
@SpringBootTest
@Transactional
@Rollback
class CarServiceTest {

    private CarService carService;
    private CarRepository carRepository;
    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    public CarServiceTest(CarService carService, CarRepository carRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
    }

    @Test
    void getAvailableCarsWhenCarsHaveNoActiveOrders() {

        Car car = new Car(990.0, "BMW", "520", "PRE580", false);
        car.setOrders(null);
        car.setId(1L);
        CarDTO carDTO = new CarDTO(car);

        carRepository.save(car);
        List<CarDTO> cars = carService.getAvailableCars();

        assertEquals(carDTO.getPlateNumber(), cars.get(0).getPlateNumber());
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
    void updateCarWhenIdExistsShouldUpdateCar() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);
        carRepository.save(car);
        car.setModel("525i");
        carService.updateCar(car);
        List<Car> cars = carRepository.findAll();

        assertThat(cars.contains(car)).isTrue();
    }

    @Test
    void updateCarShouldReturnResourceNotFoundException() {
//
//        Car car = new Car(990.0,"BMW","520","PRE580",false);
////        Car carNotSaved = new Car(990.0,"BMW","520","PRE580",false);
//        carRepository.save(car);
//        car.setId(2L);
//
//        assertThrows(ResourceNotFoundException.class, () -> carService.updateCar(car));
//

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

        long id = 100;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->carService.removeCar(id));
        assertThat(exception.getMessage()).isEqualTo("ID not found");
    }

    @Test
    void removeCarWithActiveOrdersShouldThrowException() {

//        Car car = new Car(990.0,"BMW","520","PRE580",true);
//        Order order = new Order();
//        order.setActive(true);
//        order.setCar(car);
//
//        orderRepository.save(order);
//        Car carToRemove = carRepository.save(car);
//        carToRemove.setOrders();
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.removeCar(carToRemove.getId()));
//        assertThat(exception.getMessage().contains("Admin tried to remove a car that has active orders")).isTrue();
    }

    @Test
    void removeCarInServiceShouldThrowException() {

        Car car = new Car(990.0,"BMW","520","PRE580",true);

        Car carToRemove = carRepository.save(car);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.removeCar(carToRemove.getId()));
        assertThat(exception.getMessage().contains("Car is in service and can not be removed")).isTrue();
    }
}