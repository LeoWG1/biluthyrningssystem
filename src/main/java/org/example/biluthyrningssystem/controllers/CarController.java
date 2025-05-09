package org.example.biluthyrningssystem.controllers;

import org.example.biluthyrningssystem.models.dtos.CarDTO;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Ann-Louis made this class
@RestController
@RequestMapping("/api/v1")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDTO>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @GetMapping("/admin/cars")
    public ResponseEntity<List<CarDTO>> adminGetAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @GetMapping("/admin/allcars")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @PostMapping("/admin/addcar")
    public ResponseEntity<String> addCar(@RequestBody Car car) {
        return new ResponseEntity<>(carService.addCar(car), HttpStatus.CREATED);
    }

    @PutMapping("/admin/updatecar")
    public ResponseEntity<String> updateCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(car));
    }

    @DeleteMapping("admin/removecar/{id}")
    public ResponseEntity<String> removeCar(@PathVariable long id) {
        return ResponseEntity.ok(carService.removeCar(id));
    }
}
