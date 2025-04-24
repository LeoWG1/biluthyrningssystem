package org.example.biluthyrningssystem.services;

import org.example.biluthyrningssystem.entities.Car;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService implements CarServiceInterface {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    //FUNGERAR, men hur inte visa booked/inservice?
    @Override
    public List<Car> getAvailableCars() {
        List<Car> cars = new ArrayList<>();
        for(Car car : carRepository.findAll()) {
            if(!car.isInService() && !car.isBooked()) {
                cars.add(car);
            }
        }
        return cars;
    }

    @Override
    public List<Car> adminGetAvailableCars() {
        List<Car> cars = new ArrayList<>();
        for(Car car : carRepository.findAll()) {
            if(!car.isInService() && !car.isBooked()) {
                cars.add(car);
            }
        }
        return cars;
    }


    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public String addCar(Car car) {
        for(Car carToAdd : carRepository.findAll()) {
            if(carToAdd.getPlateNumber().equals(car.getPlateNumber())) {
                throw new RuntimeException("PlateNumber already exists");
            }
            carRepository.save(car);
        }
        return "Car added";
    }

    @Override
    public String updateCar(Car car) {
        if(carRepository.existsById(car.getId())) {
            if(car.getPricePerDay() <= 0) {
                throw new RuntimeException("Invalid price per day");
            }
            else if(car.getBrand() == null) {
                throw new RuntimeException("Brand is missing");
            }
            else if(car.getModel() == null) {
                throw new RuntimeException("Model is missing");
            }
            else if(car.getPlateNumber() == null) {
                throw new RuntimeException("PlateNumber is missing");
            }
        }
        carRepository.save(car);
        return "Car updated";
    }

    @Override
    public String removeCar(Long id) {
        carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car", "id", id));
        carRepository.deleteById(id);
        return "Car removed";
    }
}
