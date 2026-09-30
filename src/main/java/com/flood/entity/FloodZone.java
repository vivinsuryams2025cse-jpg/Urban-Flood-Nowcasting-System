package com.flood.entity;

/**
 * FloodZone.java
 * 
 * Day 5 Feature: Flood Zone Classification Module
 * 
 * Classifies an urban area or drainage catchment into one of three distinct flood zones:
 * 1. SAFE    - Water levels are well within drainage limits, normal flow, no flooding threat.
 * 2. WARNING - Elevated water level or moderate runoff stress, caution advised.
 * 3. DANGER  - Overflow imminent or underway, capacity breached, severe flood hazard.
 * 
 * Couples existing hydrological parameters:
 * - Rainfall metrics (amount, intensity, duration) from Rainfall (Day 1)
 * - Drainage metrics (capacity, water level) from Drainage (Day 2)
 * - Evaluated risk level (LOW, MEDIUM, HIGH) from FloodRisk (Day 3)
 */
public class FloodZone {
    private String location;
    private double rainfallAmount;
    private double rainfallIntensity;
    private double drainageCapacity;
    private double currentWaterLevel;
    private double totalWaterLoad;
    private double capacityUtilization;
    private String riskLevel;

    private String zoneClassification;   // "SAFE", "WARNING", "DANGER"
    private String colorIndicator;       // "GREEN", "AMBER / YELLOW", "RED"
    private String zoneDescription;
    private String safetyGuidance;

    public FloodZone() {
    }

    public FloodZone(Drainage drainage, Rainfall rainfall) {
        if (drainage != null) {
            this.location = drainage.getLocation();
            this.drainageCapacity = drainage.getCapacity();
            this.currentWaterLevel = drainage.getCurrentWaterLevel();
        } else {
            this.location = "Unknown Area";
            this.drainageCapacity = 0.0;
            this.currentWaterLevel = 0.0;
        }

        if (rainfall != null) {
            this.rainfallAmount = rainfall.getRainfallAmount();
            this.rainfallIntensity = rainfall.getIntensity();
        } else {
            this.rainfallAmount = 0.0;
            this.rainfallIntensity = 0.0;
        }

        this.totalWaterLoad = this.currentWaterLevel + this.rainfallAmount;
        if (this.drainageCapacity > 0) {
            this.capacityUtilization = (this.totalWaterLoad / this.drainageCapacity) * 100.0;
        } else {
            this.capacityUtilization = 100.0;
        }

        if (drainage != null && rainfall != null) {
            FloodRisk risk = new FloodRisk(rainfall, drainage);
            this.riskLevel = risk.getRiskLevel();
        } else {
            this.riskLevel = (this.capacityUtilization >= 85.0) ? "HIGH" : 
                             (this.capacityUtilization >= 60.0 ? "MEDIUM" : "LOW");
        }

        classifyZone();
    }

    public FloodZone(FloodRisk floodRisk) {
        if (floodRisk != null) {
            this.location = floodRisk.getLocation();
            this.rainfallIntensity = floodRisk.getRainfallIntensity();
            this.rainfallAmount = floodRisk.getRainfallIntensity() * floodRisk.getRainfallDuration();
            this.drainageCapacity = floodRisk.getDrainageCapacity();
            this.currentWaterLevel = floodRisk.getCurrentWaterLevel();
            this.totalWaterLoad = floodRisk.getTotalWaterLoad();
            this.capacityUtilization = floodRisk.getCapacityUtilization();
            this.riskLevel = floodRisk.getRiskLevel();
        } else {
            this.location = "Unknown Area";
            this.drainageCapacity = 0.0;
            this.currentWaterLevel = 0.0;
            this.totalWaterLoad = 0.0;
            this.capacityUtilization = 0.0;
            this.riskLevel = "LOW";
        }

        classifyZone();
    }

    public FloodZone(String location, double rainfallIntensity, double rainfallDuration,
                     double drainageCapacity, double currentWaterLevel) {
        this.location = (location != null && !location.trim().isEmpty()) ? location : "Custom Zone";
        this.rainfallIntensity = rainfallIntensity;
        this.rainfallAmount = rainfallIntensity * rainfallDuration;
        this.drainageCapacity = drainageCapacity;
        this.currentWaterLevel = currentWaterLevel;
        this.totalWaterLoad = currentWaterLevel + this.rainfallAmount;

        if (drainageCapacity > 0) {
            this.capacityUtilization = (this.totalWaterLoad / drainageCapacity) * 100.0;
        } else {
            this.capacityUtilization = 100.0;
        }

        FloodRisk risk = new FloodRisk(this.location, rainfallIntensity, rainfallDuration, 
                                       drainageCapacity, currentWaterLevel);
        this.riskLevel = risk.getRiskLevel();

        classifyZone();
    }

    public void classifyZone() {
        if ("HIGH".equalsIgnoreCase(riskLevel) 
                || totalWaterLoad > drainageCapacity 
                || capacityUtilization >= 85.0) {
            this.zoneClassification = "DANGER";
            this.colorIndicator = "RED";
            this.zoneDescription = "Critical flood conditions. Total water volume saturates or exceeds drainage capacity. Spillover underway or imminent.";
            this.safetyGuidance = "EVACUATE OR STAY INDOORS ON HIGH FLOORS: Avoid low-lying streets and underpasses. Do not drive through flooded waters.";
        } else if ("MEDIUM".equalsIgnoreCase(riskLevel) 
                || capacityUtilization >= 60.0 
                || (currentWaterLevel / (drainageCapacity > 0 ? drainageCapacity : 1.0) >= 0.60)
                || (rainfallIntensity >= 15.0 && capacityUtilization >= 50.0)) {
            this.zoneClassification = "WARNING";
            this.colorIndicator = "AMBER / YELLOW";
            this.zoneDescription = "Elevated flood vulnerability. Drainage network is under moderate-to-heavy pressure with localized water accumulation expected.";
            this.safetyGuidance = "BE CAUTIOUS: Stay alert for rapidly rising water. Keep stormwater pumps running and avoid parking vehicles in low zones.";
        } else {
            this.zoneClassification = "SAFE";
            this.colorIndicator = "GREEN";
            this.zoneDescription = "Normal hydrological status. Ample drainage capacity buffer available. Stormwater is discharging safely.";
            this.safetyGuidance = "NO IMMEDIATE THREAT: Routine monitoring is sufficient. The drainage system is handling current runoff effectively.";
        }
    }

    public void displayZoneReport() {
        System.out.println("==================================================");
        System.out.println("          FLOOD ZONE CLASSIFICATION REPORT        ");
        System.out.println("==================================================");
        System.out.println("Location                : " + location);
        System.out.println("---------------- HYDROLOGICAL PARAMETERS ---------");
        System.out.printf ("Drainage Capacity       : %.2f mm\n", drainageCapacity);
        System.out.printf ("Current Water Level     : %.2f mm\n", currentWaterLevel);
        System.out.printf ("Rainfall Load           : %.2f mm (Intensity: %.2f mm/hr)\n", 
                           rainfallAmount, rainfallIntensity);
        System.out.printf ("Total Water Load        : %.2f mm\n", totalWaterLoad);
        System.out.printf ("Capacity Utilization    : %.1f%%\n", capacityUtilization);
        System.out.println("Coupled Flood Risk      : [" + riskLevel + "]");
        System.out.println("----------------- CLASSIFICATION RESULT ----------");
        System.out.println("ZONE CLASSIFICATION     : [" + zoneClassification + "] (" + colorIndicator + ")");
        System.out.println("Status Description      : " + zoneDescription);
        System.out.println("Precautionary Guidance  : " + safetyGuidance);
        System.out.println("==================================================");
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getRainfallAmount() { return rainfallAmount; }
    public void setRainfallAmount(double rainfallAmount) { this.rainfallAmount = rainfallAmount; }
    public double getRainfallIntensity() { return rainfallIntensity; }
    public void setRainfallIntensity(double rainfallIntensity) { this.rainfallIntensity = rainfallIntensity; }
    public double getDrainageCapacity() { return drainageCapacity; }
    public void setDrainageCapacity(double drainageCapacity) { this.drainageCapacity = drainageCapacity; }
    public double getCurrentWaterLevel() { return currentWaterLevel; }
    public void setCurrentWaterLevel(double currentWaterLevel) { this.currentWaterLevel = currentWaterLevel; }
    public double getTotalWaterLoad() { return totalWaterLoad; }
    public double getCapacityUtilization() { return capacityUtilization; }
    public String getRiskLevel() { return riskLevel; }
    public String getZoneClassification() { return zoneClassification; }
    public String getColorIndicator() { return colorIndicator; }
    public String getZoneDescription() { return zoneDescription; }
    public String getSafetyGuidance() { return safetyGuidance; }
}
