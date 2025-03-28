package com.myproject.kbayryakov.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EditOfferDto {
    @NotNull
    private LocalDate createdOn;
    @NotNull
    private LocalDate ValidUntil;
    @NotNull
    private BigDecimal price;
    @NotBlank
    private String description;

    public EditOfferDto() {
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getValidUntil() {
        return ValidUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
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
}
