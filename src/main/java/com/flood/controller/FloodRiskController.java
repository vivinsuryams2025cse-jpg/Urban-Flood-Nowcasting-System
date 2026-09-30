package com.flood.controller;

import com.flood.dto.FloodRiskResponseDto;
import com.flood.entity.FloodZone;
import com.flood.service.FloodRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * FloodRiskController.java
 * 
 * REST controller for flood risk calculations, flood zone evaluations,
 * and historical risk inquiries.
 */
@RestController
@RequestMapping("/api/flood-risk")
public class FloodRiskController {

    private final FloodRiskService floodRiskService;

    @Autowired
    public FloodRiskController(FloodRiskService floodRiskService) {
        this.floodRiskService = floodRiskService;
    }

    /**
     * POST /api/flood-risk/calculate/{drainageId}
     * Calculates coupled flood risk for a registered drainage unit using observed rainfall.
     */
    @PostMapping("/calculate/{drainageId}")
    public ResponseEntity<FloodRiskResponseDto> calculateRiskForDrainage(@PathVariable String drainageId) {
        FloodRiskResponseDto response = floodRiskService.calculateRiskForDrainage(drainageId);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/flood-risk/calculate-custom
     * Calculates custom flood risk using 4 manual parameters.
     */
    @PostMapping("/calculate-custom")
    public ResponseEntity<FloodRiskResponseDto> calculateCustomRisk(
            @RequestParam String location,
            @RequestParam double intensity,
            @RequestParam double duration,
            @RequestParam double capacity,
            @RequestParam double waterLevel) {
        FloodRiskResponseDto response = floodRiskService.calculateCustomRisk(location, intensity, duration, capacity, waterLevel);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/flood-risk/zone/{drainageId}
     * Checks Day 5 flood zone classification (SAFE, WARNING, DANGER) for an area.
     */
    @GetMapping("/zone/{drainageId}")
    public ResponseEntity<FloodZone> checkFloodZone(@PathVariable String drainageId) {
        FloodZone zone = floodRiskService.checkFloodZone(drainageId);
        return ResponseEntity.ok(zone);
    }

    /**
     * GET /api/flood-risk/history
     * Views history of flood risk assessments.
     */
    @GetMapping("/history")
    public ResponseEntity<List<FloodRiskResponseDto>> getFloodHistory() {
        return ResponseEntity.ok(floodRiskService.getFloodRiskHistory());
    }
}
