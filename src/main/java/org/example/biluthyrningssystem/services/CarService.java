package org.example.biluthyrningssystem.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.biluthyrningssystem.models.dtos.CarDTO;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.models.entities.Order;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

//Ann-Louis made this class
@Service
public class CarService implements CarServiceInterface {

    private final CarRepository carRepository;
    private static final Logger USER_LOGGER = LogManager.getLogger("userlog");

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
    /* Ann-Louis and Frida made this method
       List cars not in service along with its booked dates to see what dates are available */
    @Override
    public List<CarDTO> getAvailableCars() {
        List<CarDTO> carDTOList = new ArrayList<>();

        // Loop through all cars NOT in service
        for (Car car : carRepository.findAll()) {
            if (!car.isInService()) {
                List<Map<String, LocalDate>> allBookedDates = new ArrayList<>();
                if(car.getOrders() != null) {
                    for (Order order : car.getOrders()) {
                        if (order.isActive()) {
                            Map<String, LocalDate> mapStartDate = new HashMap<>();
                            mapStartDate.put("startDate", order.getStartDate());

                            Map<String, LocalDate> mapEndDate = new HashMap<>();
                            mapEndDate.put("endDate", order.getEndDate());

                            allBookedDates.add(mapStartDate);
                            allBookedDates.add(mapEndDate);
                        }
                    }
                }
                if (!allBookedDates.isEmpty()) {
                    carDTOList.add(new CarDTO(car, allBookedDates));
                } else {
                    carDTOList.add(new CarDTO(car));
                }
            }
        }
        return carDTOList;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public String addCar(Car car) {
        for (Car existingCar : carRepository.findAll()) {
            if (existingCar.getPlateNumber().equals(car.getPlateNumber())) {
                USER_LOGGER.warn("Admin tried to add a car with a plate number that already exists.");
                throw new ResponseStatusException(HttpStatus.CONFLICT, "PlateNumber already exists");
            }
        }
        if (isValidCar(car)) {
            carRepository.save(car);
            USER_LOGGER.info("Admin added a car with plate number {}.", car.getPlateNumber());
            return "Car added";
        }
        else {
            USER_LOGGER.warn("Admin tried to add a car with invalid data.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing some data.");
        }
    }

    @Override
    public String updateCar(Car car) {
        if(carRepository.existsById(car.getId())) {
            if(isValidCar(car)) {
                carRepository.save(car);
                USER_LOGGER.info("Admin updated car with plate number {}.", car.getPlateNumber());
                return "Car updated";
            }
            else {
                USER_LOGGER.warn("Admin tried to update a car with invalid data.");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing some data.");
            }
        }
        else {
            USER_LOGGER.warn("Admin tried to update a car that does not exist.");
            throw new ResourceNotFoundException("Car", "ID not found", car.getId());
        }
    }

    @Override
    public String removeCar(long id) {
        LocalDate today = LocalDate.now();
        if(carRepository.existsById(id)) {
            Car carToRemove = carRepository.getCarById(id);
            List<Order> carOrders = carToRemove.getOrders();
            if(carOrders != null) {
                for (Order order : carOrders) {
                    if (order.getEndDate().isAfter(today) || order.getStartDate().isBefore(today) && order.isActive()) {
                        USER_LOGGER.warn("Admin tried to remove a car that is rented");
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is rented");
                    }
                }
            }
            if(carToRemove.isInService()) {
                USER_LOGGER.warn("Admin tried to remove a car that is in service");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is in service and can not be removed");
            }
            else {
                carRepository.deleteById(id);
                USER_LOGGER.info("Admin removed car with plate number {}.", carToRemove.getPlateNumber());
                return "Car removed";
            }
        }
        else {
            USER_LOGGER.warn("Admin tried to remove a car that does not exist.");
            throw new ResourceNotFoundException("Car", "ID not found", id);
        }
    }

    private boolean isValidCar(Car car) {
        return !(car.getPricePerDay() <= 0) &&
                car.getBrand() != null && !car.getBrand().isEmpty() &&
                car.getModel() != null && !car.getModel().isEmpty() &&
                car.getPlateNumber() != null && !car.getPlateNumber().isEmpty();
    }
}
