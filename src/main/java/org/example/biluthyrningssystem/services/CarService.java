package org.example.biluthyrningssystem.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.biluthyrningssystem.models.dtos.CarDTO;
import org.example.biluthyrningssystem.models.entities.Car;
import org.example.biluthyrningssystem.models.entities.Order;
import org.example.biluthyrningssystem.exceptions.ResourceNotFoundException;
import org.example.biluthyrningssystem.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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
//    @Override
//    public List<CarDTO> getAvailableCars() {
//        List<CarDTO> carDTOList = new ArrayList<>();
//        LocalDate startDate;
//        LocalDate endDate;
//        LocalDate today = LocalDate.now();
//
//        for (Car car : carRepository.findAll()) {
//            if (!car.isInService()) {
//                Map<String, LocalDate> dates = new HashMap<>();
//                if(!car.getOrders().isEmpty()) {
//                    for (Order order : car.getOrders()) {
//                        if (order.isActive()) {
//                            startDate = order.getStartDate();
//                            endDate = order.getEndDate();
//
//                            dates.put(startDate, endDate);
//                        }
//                        carDTOList.add(new CarDTO(car, dates));
//                    }
//                }
//                else {
//                    carDTOList.add(new CarDTO(car));
//                }
//            }
//        }
//        Comparator<CarDTO> priceComparator = (car1, car2) -> (int) (car1.getPricePerDay() - car2.getPricePerDay());
//        carDTOList.sort(priceComparator);
//        return carDTOList;
//    }

    @Override
    public List<CarDTO> getAvailableCars() {
        List<CarDTO> carList = new ArrayList<>();
        List<Map<String, String>> bookedDates = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();


        for (Car car : carRepository.findAll()) {
            if (!car.isInService()) {
                if(!car.getOrders().isEmpty()) {
                    for (Order order : car.getOrders()) {
                        String startDate = order.getStartDate().toString();
                        String endDateString = order.getEndDate().toString();
                        LocalDate endDate = order.getEndDate();
                        if (currentDate.isBefore(endDate)) {
                            Map<String, String> dates = new HashMap<>();
                            dates.put("endDate", endDateString);
                            dates.put("startDate", startDate);
                            bookedDates.add(dates);
                        }
                        else {
                            Map<String, String> noDates = new HashMap<>();
                            noDates.put("endDate", null);
                            noDates.put("startDate", null);
                            bookedDates.add(noDates);
                        }
                    }
                }
                carList.add(new CarDTO(car, bookedDates));
            }
        }
        return carList;
    }

    //LISTAR JUST NU BARA BILAR SOM INTE ÄR PÅ SERVICE! //TESTAR
    @Override
    public List<CarDTO> adminGetAvailableCars() {
        List<CarDTO> carDTOList = new ArrayList<>();
        List<Map<String, String>> bookedDates = new ArrayList<>();
        Map<String, String> dates = new HashMap<>();
        String startDateString;
        String endDateString;

        for (Car car : carRepository.findAll()) {
            if (!car.isInService()) {
                for(Order order : car.getOrders()) {
                    LocalDate endDate = order.getEndDate();
                    if (isValidDate(endDate)) {
                        startDateString = order.getStartDate().toString();
                        endDateString = order.getEndDate().toString();
                        dates.put("startDate", startDateString);
                        dates.put("endDate", endDateString);
                        bookedDates.add(dates);
                    }
                    else {
                        dates.put("startDate", null);
                        dates.put("endDate", null);
                        bookedDates.add(dates);
                    }
//                    bookedDates.add(dates);
                }
//                bookedDates.add(dates);
                carDTOList.add(new CarDTO(car, bookedDates));
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
        USER_LOGGER.warn("Admin tried to add a car with invalid data.");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing some data.");
    }

    @Override
    public String updateCar(Car car) {
        if (carRepository.existsById(car.getId())) {
            if (isValidCar(car)) {
                carRepository.save(car);
                USER_LOGGER.info("Admin updated car with plate number {}.", car.getPlateNumber());
                return "Car updated";
            }
            USER_LOGGER.warn("Admin tried to update a car with invalid data.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing some data.");
        }
        USER_LOGGER.warn("Admin tried to update a car that does not exist.");
        throw new ResourceNotFoundException("Car", "id", car.getId());
    }

    @Override
    public String removeCar(Long id) {
        if (carRepository.existsById(id)) {
            Car carToRemove = carRepository.getCarById(id);
            List<Order> carOrders = carToRemove.getOrders();
            for (Order order : carOrders) {
                if (order.isActive()) {
                    USER_LOGGER.warn("Admin tried to remove a car that is rented by a customer");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is rented by a customer and can not be removed");
                }
            }
            if (carToRemove.isInService()) {
                USER_LOGGER.warn("Admin tried to remove a car that is in service");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is in service and can not be removed");
            }

            carRepository.deleteById(id);
            USER_LOGGER.info("Admin removed car with plate number {}.", carToRemove.getPlateNumber());
            return "Car removed";
        }
        USER_LOGGER.warn("Admin tried to remove a car that does not exist.");
        throw new ResourceNotFoundException("Car", "id", id);
    }

    private boolean isValidCar(Car car) {
        return !(car.getPricePerDay() <= 0) &&
                car.getBrand() != null && !car.getBrand().isEmpty() &&
                car.getModel() != null && !car.getModel().isEmpty() &&
                car.getPlateNumber() != null && !car.getPlateNumber().isEmpty();
    }

    private boolean isValidDate(LocalDate endDate) {
        LocalDate currentDate = LocalDate.now();
        return currentDate.isBefore(endDate);
    }
}
