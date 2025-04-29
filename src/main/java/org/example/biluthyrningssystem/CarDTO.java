package org.example.biluthyrningssystem;

import org.example.biluthyrningssystem.entities.Car;

import java.io.Serializable;

public class CarDTO implements Serializable {

    private String brand;
    private String model;
    private double pricePerDay;

    public CarDTO() {}

    public CarDTO(Car car) {
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.pricePerDay = car.getPricePerDay();
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
}
