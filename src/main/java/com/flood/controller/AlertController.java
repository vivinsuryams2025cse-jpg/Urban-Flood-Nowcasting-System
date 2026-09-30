package com.flood.controller;

import com.flood.entity.FloodAlert;
import com.flood.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AlertController.java
 * 
 * REST controller for managing civil protection alerts, emergency advisories,
 * shelter routing, and evacuation orders.
 */
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * POST /api/alerts/generate/{drainageId}
     * Evaluates hydrological conditions and generates an emergency flood alert.
     */
    @PostMapping("/generate/{drainageId}")
    public ResponseEntity<FloodAlert> generateAlert(@PathVariable String drainageId) {
        FloodAlert alert = alertService.generateAlertForDrainage(drainageId);
        return new ResponseEntity<>(alert, HttpStatus.CREATED);
    }

    /**
     * GET /api/alerts
     * Retrieves all issued flood alerts.
     */
    @GetMapping
    public ResponseEntity<List<FloodAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    /**
     * GET /api/alerts/threat/{threatLevel}
     * Filters alerts by threat level (e.g., CRITICAL, ELEVATED).
     */
    @GetMapping("/threat/{threatLevel}")
    public ResponseEntity<List<FloodAlert>> getAlertsByThreat(@PathVariable String threatLevel) {
        return ResponseEntity.ok(alertService.getAlertsByThreatLevel(threatLevel));
    }
}
