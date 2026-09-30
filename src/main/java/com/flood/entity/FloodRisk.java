package com.flood.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * FloodRisk.java
 * 
 * JPA Entity representing the Flood Risk Calculation Module (Day 3).
 * Couples rainfall metrics with urban drainage capacity to evaluate flood vulnerability.
 * 
 * Inputs Combined:
 * 1. Rainfall Intensity (mm/hr)
 * 2. Rainfall Duration (hours)
 * 3. Drainage Capacity (mm)
 * 4. Current Water Level (mm)
 * 
 * Uses transparent, rule-based classification to assign one of three risk levels:
 * - LOW
 * - MEDIUM
 * - HIGH
 */
@Entity
@Table(name = "flood_risk_assessments")
public class FloodRisk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. Input Parameters
    @Column(nullable = false)
    private String location;

    @Column(name = "rainfall_intensity", nullable = false)
    private double rainfallIntensity;

    @Column(name = "rainfall_duration", nullable = false)
    private double rainfallDuration;

    @Column(name = "drainage_capacity", nullable = false)
    private double drainageCapacity;

    @Column(name = "current_water_level", nullable = false)
    private double currentWaterLevel;

    // 2. Computed Hydrological Values
    @Column(name = "total_water_load", nullable = false)
    private double totalWaterLoad;

    @Column(name = "capacity_utilization", nullable = false)
    private double capacityUtilization;

    // 3. Risk Assessment Outputs
    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    @Column(name = "risk_reason", length = 1000)
    private String riskReason;

    @Column(name = "warning_message", length = 1000)
    private String warningMessage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default No-arg constructor required by JPA.
     */
    public FloodRisk() {
    }

    /**
     * Constructor 1: Connects directly with Rainfall and Drainage objects.
     * 
     * @param rainfall Rainfall observation object
     * @param drainage Drainage channel object
     */
    public FloodRisk(Rainfall rainfall, Drainage drainage) {
        if (drainage != null) {
            this.location = drainage.getLocation();
            this.drainageCapacity = drainage.getCapacity();
            this.currentWaterLevel = drainage.getCurrentWaterLevel();
        } else {
            this.location = "Unknown Location";
            this.drainageCapacity = 0.0;
            this.currentWaterLevel = 0.0;
        }

        if (rainfall != null) {
            this.rainfallIntensity = rainfall.getIntensity();
            this.rainfallDuration = rainfall.getDuration();
        } else {
            this.rainfallIntensity = 0.0;
            this.rainfallDuration = 0.0;
        }

        calculateRisk();
    }

    /**
     * Constructor 2: Parameterized constructor for custom manual input or simulation.
     * 
     * @param location          Name of the urban area
     * @param rainfallIntensity Rainfall intensity in mm/hr
     * @param rainfallDuration  Rainfall duration in hours
     * @param drainageCapacity  Maximum drainage capacity in mm
     * @param currentWaterLevel Current water level in mm
     */
    public FloodRisk(String location, double rainfallIntensity, double rainfallDuration, 
                     double drainageCapacity, double currentWaterLevel) {
        this.location = location;
        this.rainfallIntensity = rainfallIntensity;
        this.rainfallDuration = rainfallDuration;
        this.drainageCapacity = drainageCapacity;
        this.currentWaterLevel = currentWaterLevel;
        calculateRisk();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.riskLevel == null) {
            calculateRisk();
        }
    }

    /**
     * Calculates the flood risk level using transparent rule-based logic.
     */
    public void calculateRisk() {
        double incomingRain = rainfallIntensity * rainfallDuration;
        this.totalWaterLoad = currentWaterLevel + incomingRain;

        if (drainageCapacity <= 0) {
            this.capacityUtilization = 100.0;
        } else {
            this.capacityUtilization = (totalWaterLoad / drainageCapacity) * 100.0;
        }

        // RULE 1: HIGH RISK
        if (totalWaterLoad > drainageCapacity) {
            this.riskLevel = "HIGH";
            double excess = totalWaterLoad - drainageCapacity;
            this.riskReason = String.format(
                "Total water load (%.2f mm) exceeds drainage capacity (%.2f mm) by %.2f mm.",
                totalWaterLoad, drainageCapacity, excess
            );
            this.warningMessage = "RED ALERT: Severe flooding underway! Drainage capacity breached. Evacuate low-lying areas immediately.";
        } else if (capacityUtilization >= 85.0 && rainfallIntensity >= 15.0) {
            this.riskLevel = "HIGH";
            this.riskReason = String.format(
                "Drainage is %.1f%% full and high rainfall intensity (%.2f mm/hr) is causing rapid water surge.",
                capacityUtilization, rainfallIntensity
            );
            this.warningMessage = "RED ALERT: Flash flood warning! High-intensity rainfall will overwhelm remaining drainage buffer.";
        }
        // RULE 2: MEDIUM RISK
        else if (capacityUtilization >= 65.0) {
            this.riskLevel = "MEDIUM";
            double remainingMargin = drainageCapacity - totalWaterLoad;
            this.riskReason = String.format(
                "Drainage utilization is elevated at %.1f%% with only %.2f mm buffer margin remaining.",
                capacityUtilization, remainingMargin
            );
            this.warningMessage = "AMBER ADVISORY: Moderate flood risk! Street waterlogging expected in underpasses and low zones.";
        } else if (rainfallIntensity >= 20.0 && rainfallDuration >= 1.5) {
            this.riskLevel = "MEDIUM";
            this.riskReason = String.format(
                "Persistent heavy rain (%.2f mm/hr for %.1f hrs) is placing continuous stress on the drainage network.",
                rainfallIntensity, rainfallDuration
            );
            this.warningMessage = "AMBER ADVISORY: High-intensity runoff alert. Keep stormwater pumps on standby.";
        }
        // RULE 3: LOW RISK
        else {
            this.riskLevel = "LOW";
            double reserveMargin = drainageCapacity - totalWaterLoad;
            this.riskReason = String.format(
                "Drainage has ample reserve margin (%.1f%% utilized; %.2f mm headroom available).",
                capacityUtilization, reserveMargin
            );
            this.warningMessage = "GREEN STATUS: Safe conditions. Drainage flows smoothly and is handling all rainfall safely.";
        }
    }

    /**
     * Displays a clean, structured flood risk assessment report.
     */
    public void displayRiskReport() {
        double incomingRain = rainfallIntensity * rainfallDuration;

        System.out.println("==================================================");
        System.out.println("           FLOOD RISK ASSESSMENT REPORT           ");
        System.out.println("==================================================");
        System.out.println("Location              : " + location);
        System.out.println("----------------- INPUT PARAMETERS ---------------");
        System.out.printf ("Rainfall Intensity    : %.2f mm/hr\n", rainfallIntensity);
        System.out.printf ("Rainfall Duration     : %.2f hours\n", rainfallDuration);
        System.out.printf ("Drainage Capacity     : %.2f mm\n", drainageCapacity);
        System.out.printf ("Current Water Level   : %.2f mm\n", currentWaterLevel);
        System.out.println("---------------- HYDROLOGICAL LOAD ---------------");
        System.out.printf ("Incoming Rain Volume  : %.2f mm\n", incomingRain);
        System.out.printf ("Total Water Load      : %.2f mm\n", totalWaterLoad);
        System.out.printf ("Capacity Utilization  : %.1f%%\n", capacityUtilization);
        System.out.println("----------------- RISK EVALUATION ----------------");
        System.out.println("FLOOD RISK LEVEL      : [" + riskLevel + "]");
        System.out.println("Reason for Risk       : " + riskReason);
        System.out.println("Flood Warning Message : " + warningMessage);
        System.out.println("==================================================");
    }

    // --- Getters and Setters ---

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
        calculateRisk();
    }

    public double getRainfallDuration() {
        return rainfallDuration;
    }

    public void setRainfallDuration(double rainfallDuration) {
        this.rainfallDuration = rainfallDuration;
        calculateRisk();
    }

    public double getDrainageCapacity() {
        return drainageCapacity;
    }

    public void setDrainageCapacity(double drainageCapacity) {
        this.drainageCapacity = drainageCapacity;
        calculateRisk();
    }

    public double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(double currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
        calculateRisk();
    }

    public double getTotalWaterLoad() {
        return totalWaterLoad;
    }

    public double getCapacityUtilization() {
        return capacityUtilization;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getRiskReason() {
        return riskReason;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
