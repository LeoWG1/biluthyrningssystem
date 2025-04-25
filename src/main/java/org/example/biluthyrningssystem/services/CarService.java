package org.example.biluthyrningssystem.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.biluthyrningssystem.entities.Car;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class CarService implements CarServiceInterface {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

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
        for(Car existingCar : carRepository.findAll()) {
            if (existingCar.getPlateNumber().equals(car.getPlateNumber())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "PlateNumber already exists");
            }
        }
        if(isValidCar(car)) {
            carRepository.save(car);
        }
        return "Car added";
    }

    @Override
    public String updateCar(Car car) {
        if(carRepository.existsById(car.getId())) {
            if(isValidCar(car)) {
                carRepository.save(car);
                return "Car updated";
            }
        }
        throw new ResourceNotFoundException("Car", "id", car.getId());
    }

    @Override
    public String removeCar(Long id) {
        carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car", "id", id));
        carRepository.deleteById(id);
        return "Car removed";
    }

    private boolean isValidCar(Car car) {
        if(car.getPricePerDay() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pricePerDay");
        }
        else if(car.getBrand() == null || car.getBrand().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "brand is missing");
        }
        else if(car.getModel() == null || car.getModel().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "model is missing");
        }
        else if(car.getPlateNumber() == null || car.getPlateNumber().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "plateNumber is missing");
        }
        return true;
    }
}
