package com.flood;

/**
 * Drainage.java
 * 
 * Represents an urban drainage channel, culvert, or stormwater drain.
 * Stores drainage ID, location, maximum capacity, current water level, and operational status.
 * Provides methods to:
 * - Determine drainage operational status (NORMAL, WARNING, OVERFLOW RISK)
 * - Calculate whether the drainage system can handle incoming rainfall
 * - Perform coupled rainfall-drainage nowcasting analysis
 */
public class Drainage {
    // 1. Instance Variables (Attributes)
    private String drainageId;        // Unique Drainage identifier (e.g., "DRN-101")
    private String location;          // Location / catchment area (e.g., "Sector 4 Central Canal")
    private double capacity;          // Maximum water capacity in millimeters (mm)
    private double currentWaterLevel;  // Current baseline water level in millimeters (mm)
    private String status;             // Operational status: "NORMAL", "WARNING", "OVERFLOW RISK"

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
     * Formula:
     * (Current Water Level + Rainfall Amount) <= Drainage Capacity
     * 
     * @param rainfallAmount Total rainfall to be handled in mm
     * @return true if total projected water <= capacity; false otherwise
     */
    public boolean canHandleRainfall(double rainfallAmount) {
        return (currentWaterLevel + rainfallAmount) <= capacity;
    }

    /**
     * Evaluates the projected status when coupled with a specific rainfall amount:
     * - Total Water <= 60% Capacity      -> NORMAL
     * - 60% < Total Water <= 100% Capacity -> WARNING
     * - Total Water > 100% Capacity      -> OVERFLOW RISK (Urban flooding occurs)
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

    // --- Getters and Setters ---

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

    /**
     * Displays baseline details of this drainage system in a clean, formatted block.
     */
    public void displayDetails() {
        double usagePercentage = (capacity > 0) ? (currentWaterLevel / capacity) * 100.0 : 100.0;
        System.out.println("--------------------------------------------------");
        System.out.println("Drainage ID      : " + drainageId);
        System.out.println("Location         : " + location);
        System.out.printf("Max Capacity     : %.2f mm\n", capacity);
        System.out.printf("Current Water    : %.2f mm (%.1f%% filled)\n", currentWaterLevel, usagePercentage);
        System.out.println("Operating Status : " + status);
        System.out.println("--------------------------------------------------");
    }

    /**
     * Displays a detailed coupled nowcasting analysis showing how this drainage
     * responds to an incoming rainfall event.
     * 
     * @param rainfallAmount Incoming rainfall amount in mm
     */
    public void displayCoupledAnalysis(double rainfallAmount) {
        double totalWater = currentWaterLevel + rainfallAmount;
        boolean canHandle = canHandleRainfall(rainfallAmount);
        String coupledStatus = evaluateCoupledStatus(rainfallAmount);
        double utilization = (capacity > 0) ? (totalWater / capacity) * 100.0 : 100.0;

        System.out.println("==================================================");
        System.out.println("   DRAINAGE & RAINFALL COUPLING NOWCAST REPORT    ");
        System.out.println("==================================================");
        System.out.println("Drainage ID        : " + drainageId);
        System.out.println("Location           : " + location);
        System.out.printf("Drainage Capacity  : %.2f mm\n", capacity);
        System.out.printf("Current Water Level: %.2f mm\n", currentWaterLevel);
        System.out.printf("Incoming Rainfall  : %.2f mm\n", rainfallAmount);
        System.out.printf("Total Water Load   : %.2f mm\n", totalWater);
        System.out.printf("Capacity Load      : %.1f%%\n", utilization);
        System.out.println("--------------------------------------------------");
        
        if (canHandle) {
            double remainingMargin = capacity - totalWater;
            System.out.println("Can Handle Rain?   : YES [SAFE]");
            System.out.printf("Buffer Margin Left : %.2f mm\n", remainingMargin);
        } else {
            double excessOverflow = totalWater - capacity;
            System.out.println("Can Handle Rain?   : NO [ALERT]");
            System.out.printf("Projected Overflow : %.2f mm (Water spilling over!)\n", excessOverflow);
        }

        System.out.println("Coupled Status     : " + coupledStatus);
        System.out.println("==================================================");
    }
}
