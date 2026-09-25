package com.flood;

/**
 * FloodRisk.java
 * 
 * Represents the Flood Risk Calculation Module (Day 3).
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
 * 
 * Provides clear reasons and actionable flood warnings for citizens and city operators.
 */
public class FloodRisk {
    // 1. Input Parameters
    private String location;          // Location or catchment area name
    private double rainfallIntensity; // Intensity in mm/hour
    private double rainfallDuration;  // Duration in hours
    private double drainageCapacity;  // Maximum capacity in mm
    private double currentWaterLevel; // Current baseline water level in mm

    // 2. Computed Hydrological Values
    private double totalWaterLoad;      // Baseline water + incoming rain (mm)
    private double capacityUtilization; // Utilization percentage (%)

    // 3. Risk Assessment Outputs
    private String riskLevel;       // "LOW", "MEDIUM", "HIGH"
    private String riskReason;      // Plain-language reason for the risk level
    private String warningMessage;  // Actionable warning / alert message

    /**
     * Constructor 1: Connects directly with existing Rainfall and Drainage objects.
     * Demonstrates object coupling and modular design.
     * 
     * @param rainfall Rainfall observation object (Day 1)
     * @param drainage Drainage channel object (Day 2)
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

    /**
     * Calculates the flood risk level using transparent rule-based logic.
     * 
     * Step 1: Calculate incoming rainfall volume = Intensity * Duration.
     * Step 2: Calculate total water load = Current Water Level + Incoming Rain.
     * Step 3: Compute capacity utilization percentage = (Total Load / Capacity) * 100.
     * Step 4: Apply rule-based thresholds to assign LOW, MEDIUM, or HIGH risk.
     */
    public void calculateRisk() {
        // Incoming rain in mm = intensity (mm/hr) * duration (hr)
        double incomingRain = rainfallIntensity * rainfallDuration;

        // Total water the drainage must carry
        this.totalWaterLoad = currentWaterLevel + incomingRain;

        // Capacity utilization percentage
        if (drainageCapacity <= 0) {
            this.capacityUtilization = 100.0;
        } else {
            this.capacityUtilization = (totalWaterLoad / drainageCapacity) * 100.0;
        }

        // -------------------------------------------------------------
        // RULE-BASED RISK CLASSIFICATION LOGIC
        // -------------------------------------------------------------
        
        // RULE 1: HIGH RISK
        // Condition A: Total water exceeds capacity (Overflow occurring)
        // Condition B: Near saturation (>= 85%) with heavy rainfall intensity (>= 15 mm/hr)
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
        // Condition A: Capacity utilization between 65% and 100%
        // Condition B: Prolonged intense rain (>= 20 mm/hr for >= 1.5 hrs) creating runoff stress
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
        // Safe conditions: Utilization is low (< 65%) and within capacity limits
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
        System.out.printf("Rainfall Intensity    : %.2f mm/hr\n", rainfallIntensity);
        System.out.printf("Rainfall Duration     : %.2f hours\n", rainfallDuration);
        System.out.printf("Drainage Capacity     : %.2f mm\n", drainageCapacity);
        System.out.printf("Current Water Level   : %.2f mm\n", currentWaterLevel);
        System.out.println("---------------- HYDROLOGICAL LOAD ---------------");
        System.out.printf("Incoming Rain Volume  : %.2f mm\n", incomingRain);
        System.out.printf("Total Water Load      : %.2f mm\n", totalWaterLoad);
        System.out.printf("Capacity Utilization  : %.1f%%\n", capacityUtilization);
        System.out.println("----------------- RISK EVALUATION ----------------");
        System.out.println("FLOOD RISK LEVEL      : [" + riskLevel + "]");
        System.out.println("Reason for Risk       : " + riskReason);
        System.out.println("Flood Warning Message : " + warningMessage);
        System.out.println("==================================================");
    }

    // --- Getters and Setters ---

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
}
