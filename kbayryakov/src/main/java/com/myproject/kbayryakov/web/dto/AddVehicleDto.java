package com.myproject.kbayryakov.web.dto;

import jakarta.validation.constraints.NotNull;

public class AddVehicleDto {

    @NotNull
    private String type;

    @NotNull
    private String model;

    @NotNull
    private String brand;

    @NotNull
    private Integer year;

    @NotNull
    private Integer mileage;

    @NotNull
    private String transmission;

    @NotNull
    private String euroStandard;

    @NotNull
    private String fuelType;

    @NotNull
    private String color;

    public AddVehicleDto() {
    }

    public @NotNull String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public @NotNull String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public @NotNull String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public @NotNull Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public @NotNull Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public @NotNull String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public @NotNull String getEuroStandard() {
        return euroStandard;
    }

    public void setEuroStandard(String euroStandard) {
        this.euroStandard = euroStandard;
    }

    public @NotNull String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public @NotNull String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
