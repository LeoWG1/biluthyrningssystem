package org.example.biluthyrningssystem.services;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.models.dtos.CarDTO;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//Made by Ann-Louis
@SpringBootTest
@Transactional
@Rollback
class CarServiceTest {

    private final CarService carService;
    private final CarRepository carRepository;

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
    void addCarWithValidDataShouldAddCar() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        carService.addCar(car);
        List<Car> cars = carRepository.findAll();

        assertThat(cars.contains(car)).isTrue();
    }

    @Test
    void addCarShouldReturnStatusCodeConflict() {

        Car car = new Car(1200.0,"BMW","520","PRE580",false);
        Car additionalCar = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(car);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.addCar(additionalCar));
        Assertions.assertThat(exception.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)).isTrue();
        assertTrue(exception.getMessage().contains("PlateNumber already exists"));
    }

    @Test
    void addCarWithInvalidDataShouldThrowException() {

        Car car = new Car(0,"BMW","520","PRE580",false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.addCar(car));

        assertThat(exception.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)).isTrue();
        assertTrue(exception.getMessage().contains("Missing some data"));
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
    void updateCarWithInvalidDataShouldThrowException() {
        Car car = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(car);
        car.setPricePerDay(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.updateCar(car));
        assertThat(exception.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)).isTrue();
        assertTrue(exception.getMessage().contains("Missing some data"));
    }

    @Test
    void updateCarWhenIdDoesNotExistShouldThrowException() {

        Car savedCar = new Car(990.0,"BMW","520","PRE580",false);

        carRepository.save(savedCar);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
        {
            carRepository.deleteById(savedCar.getId());
            carService.updateCar(savedCar);
        });
        assertTrue(exception.getMessage().contains("ID not found"));
    }

    @Test
    void removeCarWhenIdExistsShouldRemoveCar() {

        Car car = new Car(990.0,"BMW","520","PRE580",false);

        Car carToRemove = carRepository.save(car);
        carService.removeCar(carToRemove.getId());
        List<Car> cars = carRepository.findAll();

        assertThat(cars.contains(carToRemove)).isFalse();
    }

    @Test
    void removeCarWhenIdDoesNotExistsShouldThrowException() {

        long id = 100;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->carService.removeCar(id));
        assertThat(exception.getMessage()).isEqualTo("ID not found");
    }

    @Test
    void removeCarThatIsRentedShouldThrowException() {

        Car carToRemove = carRepository.getCarById(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.removeCar(carToRemove.getId()));
        assertThat(exception.getMessage().contains("Car is rented")).isTrue();
    }

    @Test
    void removeCarInServiceShouldThrowException() {

        Car car = new Car(990.0,"BMW","520","PRE580",true);

        Car carToRemove = carRepository.save(car);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> carService.removeCar(carToRemove.getId()));
        assertThat(exception.getMessage().contains("Car is in service and can not be removed")).isTrue();
    }
}