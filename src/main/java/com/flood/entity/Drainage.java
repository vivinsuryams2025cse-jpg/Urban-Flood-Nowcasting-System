package com.flood.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Drainage.java
 * 
 * JPA Entity representing an urban drainage channel, culvert, or stormwater drain.
 * Stores drainage ID, location, maximum capacity, current water level, and operational status.
 * Provides methods to:
 * - Determine drainage operational status (NORMAL, WARNING, OVERFLOW RISK)
 * - Calculate whether the drainage system can handle incoming rainfall
 * - Perform coupled rainfall-drainage nowcasting analysis
 */
@Entity
@Table(name = "drainage_systems")
public class Drainage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drainage_id", unique = true, nullable = false)
    private String drainageId;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double capacity;

    @Column(name = "current_water_level", nullable = false)
    private double currentWaterLevel;

    @Column(nullable = false)
    private String status;

    /**
     * Default No-arg constructor required by JPA.
     */
    public Drainage() {
    }

    /**
     * Parameterized Constructor to initialize a new Drainage system.
     * 
     * @param drainageId        Unique ID of the drainage unit
     * @param location          Name of the location or area
     * @param capacity          Maximum capacity in mm
     * @param currentWaterLevel Current water level in mm
     */
    public Drainage(String drainageId, String location, double capacity, double currentWaterLevel) {
        this.drainageId = drainageId;
        this.location = location;
        this.capacity = capacity;
        this.currentWaterLevel = currentWaterLevel;
        this.status = calculateStatus();
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = calculateStatus();
        }
    }

    /**
     * Calculates the operational status based on capacity utilization percentage:
     * - Less than 60% capacity utilization     -> NORMAL (Safe drainage operation)
     * - 60% to 90% capacity utilization        -> WARNING (Water level elevated, monitor closely)
     * - Greater than 90% capacity utilization   -> OVERFLOW RISK (Spillover/backflow imminent)
     */
    public String calculateStatus() {
        if (capacity <= 0) {
            return "OVERFLOW RISK";
        }
        double usagePercentage = (currentWaterLevel / capacity) * 100.0;
        if (usagePercentage < 60.0) {
            return "NORMAL";
        } else if (usagePercentage >= 60.0 && usagePercentage <= 90.0) {
            return "WARNING";
        } else {
            return "OVERFLOW RISK";
        }
    }

    /**
     * Calculates whether the drainage system can safely handle incoming rainfall.
     * Formula: (Current Water Level + Rainfall Amount) <= Drainage Capacity
     * 
     * @param rainfallAmount Total rainfall to be handled in mm
     * @return true if total projected water <= capacity; false otherwise
     */
    public boolean canHandleRainfall(double rainfallAmount) {
        return (currentWaterLevel + rainfallAmount) <= capacity;
    }

    /**
     * Evaluates the projected status when coupled with a specific rainfall amount:
     * - Total Water <= 60% Capacity        -> NORMAL
     * - 60% < Total Water <= 100% Capacity  -> WARNING
     * - Total Water > 100% Capacity        -> OVERFLOW RISK (Urban flooding occurs)
     * 
     * @param rainfallAmount Rainfall amount in mm
     * @return Projected status string
     */
    public String evaluateCoupledStatus(double rainfallAmount) {
        if (capacity <= 0) {
            return "OVERFLOW RISK";
        }
        double totalWater = currentWaterLevel + rainfallAmount;
        double projectedUsage = (totalWater / capacity) * 100.0;

        if (projectedUsage < 60.0) {
            return "NORMAL";
        } else if (projectedUsage >= 60.0 && projectedUsage <= 100.0) {
            return "WARNING";
        } else {
            return "OVERFLOW RISK";
        }
    }

    /**
     * Displays a clean console breakdown of this drainage unit.
     */
    public void displayDetails() {
        double usagePercentage = (capacity > 0) ? (currentWaterLevel / capacity) * 100.0 : 100.0;
        System.out.println("--------------------------------------------------");
        System.out.println("Drainage ID      : " + drainageId);
        System.out.println("Location         : " + location);
        System.out.printf ("Max Capacity     : %.2f mm\n", capacity);
        System.out.printf ("Current Water    : %.2f mm (%.1f%% filled)\n", currentWaterLevel, usagePercentage);
        System.out.println("Operating Status : " + status);
        System.out.println("--------------------------------------------------");
    }

    /**
     * Displays coupled analysis for a specific rainfall amount.
     */
    public void displayCoupledAnalysis(double rainfallAmount) {
        double totalWater = currentWaterLevel + rainfallAmount;
        double remainingCapacity = capacity - totalWater;
        boolean canHandle = canHandleRainfall(rainfallAmount);
        String projectedStatus = evaluateCoupledStatus(rainfallAmount);

        System.out.println("\n--------------------------------------------------");
        System.out.println("        COUPLED RAINFALL-DRAINAGE ANALYSIS        ");
        System.out.println("--------------------------------------------------");
        System.out.println("Drainage ID              : " + drainageId);
        System.out.println("Location                 : " + location);
        System.out.printf ("Drainage Max Capacity    : %.2f mm\n", capacity);
        System.out.printf ("Baseline Water Level     : %.2f mm\n", currentWaterLevel);
        System.out.printf ("Incoming Rainfall Volume : %.2f mm\n", rainfallAmount);
        System.out.printf ("Total Projected Water    : %.2f mm\n", totalWater);
        System.out.printf ("Remaining Buffer Margin  : %.2f mm\n", remainingCapacity);
        System.out.println("Can Handle Rainfall?     : " + (canHandle ? "YES (SAFE)" : "NO (OVERFLOW HAZARD)"));
        System.out.println("Projected System Status  : " + projectedStatus);
        System.out.println("--------------------------------------------------");
    }

    // --- Getters and Setters ---

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
        this.status = calculateStatus();
    }

    public double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(double currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
        this.status = calculateStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
