package org.example.biluthyrningssystem.repositories;

import org.example.biluthyrningssystem.models.entities.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//Made by Ann-Louis
@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    void getCarByIdShouldReturnCar() {
        //Given
        Car car = new Car(500.0,"BMW","520","PRE520",false);
        //When
        Car savedCar = carRepository.save(car);
        Car foundCar = carRepository.getCarById(savedCar.getId());
        //Then
        assertThat(foundCar.getPlateNumber()).isEqualTo(car.getPlateNumber());
    }
}