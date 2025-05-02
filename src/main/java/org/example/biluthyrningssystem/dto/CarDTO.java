package org.example.biluthyrningssystem.dto;

import org.example.biluthyrningssystem.entities.Car;
import org.example.biluthyrningssystem.entities.Order;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//Ann-Louis made this class
public class CarDTO implements Serializable {

    private String brand;
    private String model;
    private double pricePerDay;
    private Map<LocalDate, LocalDate> bookedDates;

    public CarDTO(Car car) {
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.pricePerDay = car.getPricePerDay();
    }

    public CarDTO(Car car, Map<LocalDate, LocalDate> bookedDates) {
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

    public Map<LocalDate, LocalDate> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(Map<LocalDate, LocalDate> bookedDates) {
        this.bookedDates = bookedDates;
    }
}
