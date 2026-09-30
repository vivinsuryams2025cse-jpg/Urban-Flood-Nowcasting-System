package com.flood.dto;

import java.time.LocalDateTime;

/**
 * RainfallResponseDto.java
 * 
 * Data Transfer Object for returning rainfall observation details to clients.
 */
public class RainfallResponseDto {

    private Long id;
    private String location;
    private double rainfallAmount;
    private double duration;
    private double intensity;
    private String intensityLevel;
    private LocalDateTime createdAt;

    public RainfallResponseDto() {
    }

    public RainfallResponseDto(Long id, String location, double rainfallAmount, double duration, 
                               double intensity, String intensityLevel, LocalDateTime createdAt) {
        this.id = id;
        this.location = location;
        this.rainfallAmount = rainfallAmount;
        this.duration = duration;
        this.intensity = intensity;
        this.intensityLevel = intensityLevel;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public String getIntensityLevel() {
        return intensityLevel;
    }

    public void setIntensityLevel(String intensityLevel) {
        this.intensityLevel = intensityLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
