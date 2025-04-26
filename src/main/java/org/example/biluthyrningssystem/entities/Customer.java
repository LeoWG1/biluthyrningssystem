package org.example.biluthyrningssystem.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer { // Entire Class made by Leo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 30, unique=true, updatable = false, nullable=false)
    private String socialSecurityNumber;
    @Column(length = 30, nullable=false)
    private String firstName;
    @Column(length = 30, nullable=false)
    private String lastName;
    @Column(length = 30)
    private String email;
    @Column(length = 30)
    private String address;
    @Column(length = 30)
    private String phoneNumber;

    public Customer(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
