package com.flood.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * RainfallRequestDto.java
 * 
 * Data Transfer Object for creating or submitting rainfall observation data.
 */
public class RainfallRequestDto {

    @NotBlank(message = "Location is required")
    private String location;

    @PositiveOrZero(message = "Rainfall amount must be zero or positive")
    private double rainfallAmount;

    @Positive(message = "Duration must be greater than zero hours")
    private double duration;

    public RainfallRequestDto() {
    }

    public RainfallRequestDto(String location, double rainfallAmount, double duration) {
        this.location = location;
        this.rainfallAmount = rainfallAmount;
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRainfallAmount() {
        return rainfallAmount;
    }

    public void setRainfallAmount(double rainfallAmount) {
        this.rainfallAmount = rainfallAmount;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
