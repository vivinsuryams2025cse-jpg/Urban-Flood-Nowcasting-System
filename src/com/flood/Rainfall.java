package com.flood;

/**
 * Rainfall.java
 * 
 * Represents rainfall observation data at a specific urban location.
 * Stores information like location, rainfall amount, and duration.
 * Automatically calculates rainfall intensity (mm/hour) and its risk classification.
 */
public class Rainfall {
    // 1. Instance Variables (Attributes)
    private String location;        // Name of the location or area (e.g., "Main Street Basin")
    private double rainfallAmount;  // Total rainfall measured in millimeters (mm)
    private double duration;        // Duration of rainfall in hours
    private double intensity;       // Calculated rainfall intensity in mm/hour
    private String intensityLevel;  // Category: Light, Moderate, Heavy, or Torrential

    /**
     * Parameterized Constructor to initialize a new Rainfall object.
     * 
     * @param location       Name of the observation area
     * @param rainfallAmount Total rainfall in mm
     * @param duration       Rainfall duration in hours
     */
    public Rainfall(String location, double rainfallAmount, double duration) {
        this.location = location;
        this.rainfallAmount = rainfallAmount;
        this.duration = duration;
        this.intensity = calculateIntensity();
        this.intensityLevel = determineIntensityLevel();
    }

    /**
     * Calculates rainfall intensity using the standard meteorological formula:
     * Intensity = Total Rainfall Amount (mm) / Duration (hours)
     */
    private double calculateIntensity() {
        if (duration <= 0) {
            return 0.0;
        }
        return rainfallAmount / duration;
    }

    /**
     * Determines intensity category using standard meteorological thresholds:
     * - Less than 2.5 mm/hr  -> Light Rain
     * - 2.5 to 7.6 mm/hr     -> Moderate Rain
     * - 7.6 to 50.0 mm/hr    -> Heavy Rain (Drainage overload alert)
     * - Greater than 50 mm/hr -> Torrential Rain (Flash flood danger)
     */
    private String determineIntensityLevel() {
        if (intensity < 2.5) {
            return "Light";
        } else if (intensity >= 2.5 && intensity < 7.6) {
            return "Moderate";
        } else if (intensity >= 7.6 && intensity <= 50.0) {
            return "Heavy";
        } else {
            return "Torrential (Cloudburst Warning)";
        }
    }

    // --- Getters and Setters ---

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
        // Recalculate intensity whenever amount changes
        this.intensity = calculateIntensity();
        this.intensityLevel = determineIntensityLevel();
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
        // Recalculate intensity whenever duration changes
        this.intensity = calculateIntensity();
        this.intensityLevel = determineIntensityLevel();
    }

    public double getIntensity() {
        return intensity;
    }

    public String getIntensityLevel() {
        return intensityLevel;
    }

    /**
     * Displays all details of this rainfall record in a formatted block.
     */
    public void displayDetails() {
        System.out.println("--------------------------------------------------");
        System.out.println("Location         : " + location);
        System.out.printf("Rainfall Amount  : %.2f mm\n", rainfallAmount);
        System.out.printf("Duration         : %.2f hours\n", duration);
        System.out.printf("Intensity        : %.2f mm/hr\n", intensity);
        System.out.println("Intensity Level  : " + intensityLevel);
        System.out.println("--------------------------------------------------");
    }
}
