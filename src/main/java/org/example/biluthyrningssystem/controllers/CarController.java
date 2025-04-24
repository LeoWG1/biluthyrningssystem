package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.entities.Car;
import org.example.biluthyrningssystem.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    //OBS TILLGÄNGLIGA BILAR
    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getCars() {
        return carService.getCars();
    }
}
