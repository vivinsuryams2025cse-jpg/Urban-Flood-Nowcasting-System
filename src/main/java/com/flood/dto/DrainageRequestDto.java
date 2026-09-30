package com.flood.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DrainageRequestDto.java
 * 
 * Data Transfer Object for creating or registering a drainage system.
 */
public class DrainageRequestDto {

    @NotBlank(message = "Drainage ID is required (e.g. DRN-101)")
    private String drainageId;

    @NotBlank(message = "Location is required")
    private String location;

    @Positive(message = "Drainage capacity must be greater than zero mm")
    private double capacity;

    @PositiveOrZero(message = "Current water level must be zero or positive mm")
    private double currentWaterLevel;

    public DrainageRequestDto() {
    }

    public DrainageRequestDto(String drainageId, String location, double capacity, double currentWaterLevel) {
        this.drainageId = drainageId;
        this.location = location;
        this.capacity = capacity;
        this.currentWaterLevel = currentWaterLevel;
    }

    public String getDrainageId() {
        return drainageId;
    }

    public void setDrainageId(String drainageId) {
        this.drainageId = drainageId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(double currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
    }
}
