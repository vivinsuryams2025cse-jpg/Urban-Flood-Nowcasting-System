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
 * Rainfall.java
 * 
 * JPA Entity representing rainfall observation data at a specific urban location.
 * Stores information like location, rainfall amount, and duration.
 * Automatically calculates rainfall intensity (mm/hour) and its risk classification.
 */
@Entity
@Table(name = "rainfall_records")
public class Rainfall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double rainfallAmount;

    @Column(nullable = false)
    private double duration;

    @Column(nullable = false)
    private double intensity;

    @Column(nullable = false)
    private String intensityLevel;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default No-arg constructor required by JPA.
     */
    public Rainfall() {
    }

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

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.intensity == 0.0 && this.duration > 0) {
            this.intensity = calculateIntensity();
            this.intensityLevel = determineIntensityLevel();
        }
    }

    /**
     * Calculates rainfall intensity using the standard meteorological formula:
     * Intensity = Total Rainfall Amount (mm) / Duration (hours)
     */
    public double calculateIntensity() {
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
    public String determineIntensityLevel() {
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

    /**
     * Displays rainfall record in clean console format (Preserved from Day 1).
     */
    public void displayDetails() {
        System.out.println("--------------------------------------------------");
        System.out.println("Location        : " + location);
        System.out.printf ("Rainfall Amount : %.2f mm\n", rainfallAmount);
        System.out.printf ("Duration        : %.2f hours\n", duration);
        System.out.printf ("Intensity       : %.2f mm/hr\n", intensity);
        System.out.println("Category        : " + intensityLevel);
        System.out.println("--------------------------------------------------");
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

    public double getRainfallAmount() {
        return rainfallAmount;
    }

    public void setRainfallAmount(double rainfallAmount) {
        this.rainfallAmount = rainfallAmount;
        this.intensity = calculateIntensity();
        this.intensityLevel = determineIntensityLevel();
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
        this.intensity = calculateIntensity();
        this.intensityLevel = determineIntensityLevel();
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
