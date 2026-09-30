package com.flood.dto;

import java.time.LocalDateTime;

/**
 * FloodRiskResponseDto.java
 * 
 * Data Transfer Object for returning flood risk assessments,
 * coupled hydrological metrics, and flood zone classifications.
 */
public class FloodRiskResponseDto {

    private Long id;
    private String location;
    private double rainfallIntensity;
    private double rainfallDuration;
    private double drainageCapacity;
    private double currentWaterLevel;
    private double incomingRainVolume;
    private double totalWaterLoad;
    private double capacityUtilization;
    private String riskLevel;          // LOW, MEDIUM, HIGH
    private String riskReason;
    private String warningMessage;
    private String zoneClassification; // SAFE, WARNING, DANGER
    private String zoneColor;          // GREEN, AMBER / YELLOW, RED
    private LocalDateTime evaluatedAt;

    public FloodRiskResponseDto() {
    }

    public FloodRiskResponseDto(Long id, String location, double rainfallIntensity, 
                                double rainfallDuration, double drainageCapacity, 
                                double currentWaterLevel, double incomingRainVolume, 
                                double totalWaterLoad, double capacityUtilization, 
                                String riskLevel, String riskReason, String warningMessage, 
                                String zoneClassification, String zoneColor, 
                                LocalDateTime evaluatedAt) {
        this.id = id;
        this.location = location;
        this.rainfallIntensity = rainfallIntensity;
        this.rainfallDuration = rainfallDuration;
        this.drainageCapacity = drainageCapacity;
        this.currentWaterLevel = currentWaterLevel;
        this.incomingRainVolume = incomingRainVolume;
        this.totalWaterLoad = totalWaterLoad;
        this.capacityUtilization = capacityUtilization;
        this.riskLevel = riskLevel;
        this.riskReason = riskReason;
        this.warningMessage = warningMessage;
        this.zoneClassification = zoneClassification;
        this.zoneColor = zoneColor;
        this.evaluatedAt = evaluatedAt;
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

    public double getRainfallIntensity() {
        return rainfallIntensity;
    }

    public void setRainfallIntensity(double rainfallIntensity) {
        this.rainfallIntensity = rainfallIntensity;
    }

    public double getRainfallDuration() {
        return rainfallDuration;
    }

    public void setRainfallDuration(double rainfallDuration) {
        this.rainfallDuration = rainfallDuration;
    }

    public double getDrainageCapacity() {
        return drainageCapacity;
    }

    public void setDrainageCapacity(double drainageCapacity) {
        this.drainageCapacity = drainageCapacity;
    }

    public double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(double currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
    }

    public double getIncomingRainVolume() {
        return incomingRainVolume;
    }

    public void setIncomingRainVolume(double incomingRainVolume) {
        this.incomingRainVolume = incomingRainVolume;
    }

    public double getTotalWaterLoad() {
        return totalWaterLoad;
    }

    public void setTotalWaterLoad(double totalWaterLoad) {
        this.totalWaterLoad = totalWaterLoad;
    }

    public double getCapacityUtilization() {
        return capacityUtilization;
    }

    public void setCapacityUtilization(double capacityUtilization) {
        this.capacityUtilization = capacityUtilization;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskReason() {
        return riskReason;
    }

    public void setRiskReason(String riskReason) {
        this.riskReason = riskReason;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public String getZoneClassification() {
        return zoneClassification;
    }

    public void setZoneClassification(String zoneClassification) {
        this.zoneClassification = zoneClassification;
    }

    public String getZoneColor() {
        return zoneColor;
    }

    public void setZoneColor(String zoneColor) {
        this.zoneColor = zoneColor;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}
