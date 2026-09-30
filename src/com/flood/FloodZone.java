package com.flood;

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
 * 
 * Uses clear, transparent, beginner-friendly rule-based classification (No AI / ML).
 */
public class FloodZone {
    // -------------------------------------------------------------
    // 1. Instance Variables (Hydrological Parameters & Zone State)
    // -------------------------------------------------------------
    private String location;            // Name of the area or catchment
    private double rainfallAmount;       // Rainfall amount in mm
    private double rainfallIntensity;    // Rainfall intensity in mm/hr
    private double drainageCapacity;     // Maximum capacity in mm
    private double currentWaterLevel;    // Current baseline water level in mm
    private double totalWaterLoad;       // Total water load (Water Level + Rain) in mm
    private double capacityUtilization;  // Capacity utilization percentage (%)
    private String riskLevel;            // Associated Day 3 risk ("LOW", "MEDIUM", "HIGH")

    // -------------------------------------------------------------
    // 2. Zone Classification Outputs
    // -------------------------------------------------------------
    private String zoneClassification;   // "SAFE", "WARNING", "DANGER"
    private String colorIndicator;       // "GREEN", "AMBER / YELLOW", "RED"
    private String zoneDescription;      // Plain-language description of the zone status
    private String safetyGuidance;       // Actionable recommendations for the public/operators

    /**
     * Constructor 1: Couples directly with existing Drainage and Rainfall objects.
     * Uses Day 1 (Rainfall) and Day 2 (Drainage) data to compute Day 3 FloodRisk and Day 5 Zone.
     * 
     * @param drainage Drainage channel object (Day 2)
     * @param rainfall Rainfall observation object (Day 1)
     */
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

        // Calculate total load and capacity utilization
        this.totalWaterLoad = this.currentWaterLevel + this.rainfallAmount;
        if (this.drainageCapacity > 0) {
            this.capacityUtilization = (this.totalWaterLoad / this.drainageCapacity) * 100.0;
        } else {
            this.capacityUtilization = 100.0;
        }

        // Determine associated FloodRisk
        if (drainage != null && rainfall != null) {
            FloodRisk risk = new FloodRisk(rainfall, drainage);
            this.riskLevel = risk.getRiskLevel();
        } else {
            this.riskLevel = (this.capacityUtilization >= 85.0) ? "HIGH" : 
                             (this.capacityUtilization >= 60.0 ? "MEDIUM" : "LOW");
        }

        classifyZone();
    }

    /**
     * Constructor 2: Directly couples with a Day 3 FloodRisk object.
     * 
     * @param floodRisk Evaluated FloodRisk object (Day 3)
     */
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

    /**
     * Constructor 3: Parameterized constructor for custom manual input or simulation.
     * 
     * @param location          Name of the urban area
     * @param rainfallIntensity Rainfall intensity in mm/hr
     * @param rainfallDuration  Rainfall duration in hours
     * @param drainageCapacity  Drainage capacity in mm
     * @param currentWaterLevel Current water level in mm
     */
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

        // Derive FloodRisk
        FloodRisk risk = new FloodRisk(this.location, rainfallIntensity, rainfallDuration, 
                                       drainageCapacity, currentWaterLevel);
        this.riskLevel = risk.getRiskLevel();

        classifyZone();
    }

    // -------------------------------------------------------------
    // 3. Rule-Based Classification Logic (No AI / ML)
    // -------------------------------------------------------------

    /**
     * Classifies the zone into SAFE, WARNING, or DANGER using clear,
     * transparent rule-based hydrological thresholds:
     * 
     * - DANGER:
     *   1. Flood risk is "HIGH", OR
     *   2. Total water load exceeds drainage capacity (active overflow), OR
     *   3. Capacity utilization is >= 85% (impending overflow).
     * 
     * - WARNING:
     *   1. Flood risk is "MEDIUM", OR
     *   2. Capacity utilization is between 60% and 85%, OR
     *   3. Current water level is >= 60% of capacity, OR
     *   4. High rainfall intensity (>= 15 mm/hr) creates rapid surface runoff.
     * 
     * - SAFE:
     *   1. Capacity utilization is < 60%, AND
     *   2. Drainage has adequate buffer headroom, AND
     *   3. No high-risk runoff conditions.
     */
    public void classifyZone() {
        // RULE 1: DANGER ZONE
        if ("HIGH".equalsIgnoreCase(riskLevel) 
                || totalWaterLoad > drainageCapacity 
                || capacityUtilization >= 85.0) {
            this.zoneClassification = "DANGER";
            this.colorIndicator = "RED";
            this.zoneDescription = "Critical flood conditions. Total water volume saturates or exceeds drainage capacity. Spillover underway or imminent.";
            this.safetyGuidance = "EVACUATE OR STAY INDOORS ON HIGH FLOORS: Avoid low-lying streets and underpasses. Do not drive through flooded waters.";
        }
        // RULE 2: WARNING ZONE
        else if ("MEDIUM".equalsIgnoreCase(riskLevel) 
                || capacityUtilization >= 60.0 
                || (currentWaterLevel / (drainageCapacity > 0 ? drainageCapacity : 1.0) >= 0.60)
                || (rainfallIntensity >= 15.0 && capacityUtilization >= 50.0)) {
            this.zoneClassification = "WARNING";
            this.colorIndicator = "AMBER / YELLOW";
            this.zoneDescription = "Elevated flood vulnerability. Drainage network is under moderate-to-heavy pressure with localized water accumulation expected.";
            this.safetyGuidance = "BE CAUTIOUS: Stay alert for rapidly rising water. Keep stormwater pumps running and avoid parking vehicles in low zones.";
        }
        // RULE 3: SAFE ZONE
        else {
            this.zoneClassification = "SAFE";
            this.colorIndicator = "GREEN";
            this.zoneDescription = "Normal hydrological status. Ample drainage capacity buffer available. Stormwater is discharging safely.";
            this.safetyGuidance = "NO IMMEDIATE THREAT: Routine monitoring is sufficient. The drainage system is handling current runoff effectively.";
        }
    }

    // -------------------------------------------------------------
    // 4. Output Display Method
    // -------------------------------------------------------------

    /**
     * Displays a clean, structured flood zone classification report.
     */
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

    // -------------------------------------------------------------
    // 5. Getters and Setters
    // -------------------------------------------------------------

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
        recalculate();
    }

    public double getRainfallIntensity() {
        return rainfallIntensity;
    }

    public void setRainfallIntensity(double rainfallIntensity) {
        this.rainfallIntensity = rainfallIntensity;
        recalculate();
    }

    public double getDrainageCapacity() {
        return drainageCapacity;
    }

    public void setDrainageCapacity(double drainageCapacity) {
        this.drainageCapacity = drainageCapacity;
        recalculate();
    }

    public double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(double currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
        recalculate();
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

    public String getZoneClassification() {
        return zoneClassification;
    }

    public String getColorIndicator() {
        return colorIndicator;
    }

    public String getZoneDescription() {
        return zoneDescription;
    }

    public String getSafetyGuidance() {
        return safetyGuidance;
    }

    /**
     * Recalculates total load, capacity utilization, and re-classifies the zone.
     */
    private void recalculate() {
        this.totalWaterLoad = this.currentWaterLevel + this.rainfallAmount;
        if (this.drainageCapacity > 0) {
            this.capacityUtilization = (this.totalWaterLoad / this.drainageCapacity) * 100.0;
        } else {
            this.capacityUtilization = 100.0;
        }
        classifyZone();
    }
}
