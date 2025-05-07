package org.example.biluthyrningssystem.models.dtos;

import org.example.biluthyrningssystem.models.entities.Car;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//Ann-Louis made this class
public class CarDTO {

    private String brand;
    private String model;
    private double pricePerDay;
    private List<Map<String, LocalDate>> bookedDates;

    public CarDTO(Car car) {
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.pricePerDay = car.getPricePerDay();
    }

    public CarDTO(Car car, List<Map<String, LocalDate>> bookedDates) {
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.pricePerDay = car.getPricePerDay();
        this.bookedDates = bookedDates;
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

    public List<Map<String, LocalDate>> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(List<Map<String, LocalDate>> bookedDates) {
        this.bookedDates = bookedDates;
    }
}
