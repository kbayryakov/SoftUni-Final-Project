package com.myproject.kbayryakov.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private Offer offer;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    @NotBlank
    private String type;

    @Column(nullable = false)
    @NotBlank
    private String model;

    @Column(nullable = false)
    @NotBlank
    private String brand;

    @Column(name = "manufactured_year", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer year;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer mileage;

    @Column(nullable = false)
    @NotBlank
    private String transmission;

    @Column(name = "euro_standard", nullable = false)
    @NotBlank
    private String euroStandard;

    @Column(name = "fuel_type", nullable = false)
    @NotBlank
    private String fuelType;

    @Column(nullable = false)
    @NotBlank
    private String color;

    public Vehicle() {
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getEuroStandard() {
        return euroStandard;
    }

    public void setEuroStandard(String euroStandard) {
        this.euroStandard = euroStandard;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
