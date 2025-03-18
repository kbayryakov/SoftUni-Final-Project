package com.myproject.kbayryakov.web.dto;

import com.myproject.kbayryakov.models.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class AddOfferDto {
    @NotNull
    private LocalDate createdOn;
    @NotNull
    private String ValidUntil;
    @NotNull
    private BigDecimal price;
    @NotNull
    private String description;
    @NotNull
    private Vehicle vehicle;

    public AddOfferDto() {
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getValidUntil() {
        return ValidUntil;
    }

    public void setValidUntil(String validUntil) {
        ValidUntil = validUntil;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
