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
 * FloodAlert.java
 * 
 * JPA Entity representing emergency flood advisories, evacuation directives,
 * and civil protection alerts generated for specific urban catchments.
 */
@Entity
@Table(name = "flood_alerts")
public class FloodAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(name = "threat_level", nullable = false)
    private String threatLevel;

    @Column(name = "evacuation_order", nullable = false)
    private String evacuationOrder;

    @Column(name = "response_level", nullable = false)
    private String responseLevel;

    @Column(name = "assigned_shelter")
    private String assignedShelter;

    @Column(name = "primary_contact")
    private String primaryContact;

    @Column(name = "alert_message", length = 1000)
    private String alertMessage;

    @Column(name = "issued_at", updatable = false)
    private LocalDateTime issuedAt;

    /**
     * Default No-arg constructor required by JPA.
     */
    public FloodAlert() {
    }

    /**
     * Parameterized Constructor.
     */
    public FloodAlert(String location, String threatLevel, String evacuationOrder, 
                      String responseLevel, String assignedShelter, String primaryContact, 
                      String alertMessage) {
        this.location = location;
        this.threatLevel = threatLevel;
        this.evacuationOrder = evacuationOrder;
        this.responseLevel = responseLevel;
        this.assignedShelter = assignedShelter;
        this.primaryContact = primaryContact;
        this.alertMessage = alertMessage;
    }

    @PrePersist
    protected void onCreate() {
        if (this.issuedAt == null) {
            this.issuedAt = LocalDateTime.now();
        }
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

    public String getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(String threatLevel) {
        this.threatLevel = threatLevel;
    }

    public String getEvacuationOrder() {
        return evacuationOrder;
    }

    public void setEvacuationOrder(String evacuationOrder) {
        this.evacuationOrder = evacuationOrder;
    }

    public String getResponseLevel() {
        return responseLevel;
    }

    public void setResponseLevel(String responseLevel) {
        this.responseLevel = responseLevel;
    }

    public String getAssignedShelter() {
        return assignedShelter;
    }

    public void setAssignedShelter(String assignedShelter) {
        this.assignedShelter = assignedShelter;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
}
