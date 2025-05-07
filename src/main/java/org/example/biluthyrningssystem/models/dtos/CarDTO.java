package org.example.biluthyrningssystem.models.dtos;

import org.example.biluthyrningssystem.models.entities.Car;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//Ann-Louis made this class
public class CarDTO {

    private long id;
    private String brand;
    private String model;
    private double pricePerDay;
    private boolean inService;
    private List<Map<String, LocalDate>> bookedDates;

    public CarDTO(Car car) {
        this.id = car.getId();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.pricePerDay = car.getPricePerDay();
        this.inService = car.isInService();
    }

    public CarDTO(Car car, List<Map<String, LocalDate>> bookedDates) {
        this.id = car.getId();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.pricePerDay = car.getPricePerDay();
        this.inService = car.isInService();
        this.bookedDates = bookedDates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    public List<Map<String, LocalDate>> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(List<Map<String, LocalDate>> bookedDates) {
        this.bookedDates = bookedDates;
    }
}
