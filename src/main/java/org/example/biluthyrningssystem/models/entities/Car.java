package org.example.biluthyrningssystem.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

// Ann-Louis made this class
@Entity
@Table(name = "cars")
@JsonIgnoreProperties("orders")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private double pricePerDay;

    @Column(length = 20, nullable = false)
    private String brand;

    @Column(length = 20, nullable = false)
    private String model;

    @Column(length = 20, nullable = false)
    private String plateNumber;

    @Column(length = 10, nullable = false)
    private boolean inService;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    public Car() {}

    public Car(double pricePerDay, String brand, String model, String plateNumber, boolean inService) {
        this.pricePerDay = pricePerDay;
        this.brand = brand;
        this.model = model;
        this.plateNumber = plateNumber;
        this.inService = inService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
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

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
