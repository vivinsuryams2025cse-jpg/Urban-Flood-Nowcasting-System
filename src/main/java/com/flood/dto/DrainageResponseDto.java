package com.flood.dto;

/**
 * DrainageResponseDto.java
 * 
 * Data Transfer Object for returning drainage system metrics and status.
 */
public class DrainageResponseDto {

    private Long id;
    private String drainageId;
    private String location;
    private double capacity;
    private double currentWaterLevel;
    private double utilizationPercentage;
    private String status;

    public DrainageResponseDto() {
    }

    public DrainageResponseDto(Long id, String drainageId, String location, double capacity, 
                               double currentWaterLevel, double utilizationPercentage, String status) {
        this.id = id;
        this.drainageId = drainageId;
        this.location = location;
        this.capacity = capacity;
        this.currentWaterLevel = currentWaterLevel;
        this.utilizationPercentage = utilizationPercentage;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getUtilizationPercentage() {
        return utilizationPercentage;
    }

    public void setUtilizationPercentage(double utilizationPercentage) {
        this.utilizationPercentage = utilizationPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
