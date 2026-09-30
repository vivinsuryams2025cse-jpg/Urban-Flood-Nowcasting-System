package com.flood.controller;

import com.flood.dto.DrainageRequestDto;
import com.flood.dto.DrainageResponseDto;
import com.flood.service.DrainageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DrainageController.java
 * 
 * REST controller for urban drainage system operations.
 * Provides endpoints to register drainage units, inspect capacity utilization,
 * update and monitor water levels, and view overflow warning zones.
 */
@RestController
@RequestMapping("/api/drainage")
public class DrainageController {

    private final DrainageService drainageService;

    @Autowired
    public DrainageController(DrainageService drainageService) {
        this.drainageService = drainageService;
    }

    /**
     * POST /api/drainage
     * Adds and registers a new drainage system.
     */
    @PostMapping
    public ResponseEntity<DrainageResponseDto> addDrainage(@Valid @RequestBody DrainageRequestDto requestDto) {
        DrainageResponseDto responseDto = drainageService.addDrainage(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * GET /api/drainage
     * Retrieves all registered drainage systems.
     */
    @GetMapping
    public ResponseEntity<List<DrainageResponseDto>> getAllDrainages() {
        return ResponseEntity.ok(drainageService.getAllDrainages());
    }

    /**
     * GET /api/drainage/{drainageId}
     * Retrieves a drainage system by ID (e.g. DRN-101).
     */
    @GetMapping("/{drainageId}")
    public ResponseEntity<DrainageResponseDto> getDrainageById(@PathVariable String drainageId) {
        return ResponseEntity.ok(drainageService.getDrainageByDrainageId(drainageId));
    }

    /**
     * GET /api/drainage/overflow-risk
     * Displays all drainage systems currently under WARNING or OVERFLOW RISK.
     */
    @GetMapping("/overflow-risk")
    public ResponseEntity<List<DrainageResponseDto>> getOverflowRiskDrainages() {
        return ResponseEntity.ok(drainageService.getOverflowRiskDrainages());
    }

    /**
     * PUT /api/drainage/{drainageId}/water-level?level=35.0
     * Updates and monitors current water level of a drainage channel.
     */
    @PutMapping("/{drainageId}/water-level")
    public ResponseEntity<DrainageResponseDto> updateWaterLevel(@PathVariable String drainageId,
                                                               @RequestParam("level") double level) {
        DrainageResponseDto updated = drainageService.updateWaterLevel(drainageId, level);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /api/drainage/{drainageId}/water-level
     * Monitors the current water level and utilization percentage for a drainage system.
     */
    @GetMapping("/{drainageId}/water-level")
    public ResponseEntity<Map<String, Object>> monitorWaterLevel(@PathVariable String drainageId) {
        DrainageResponseDto dto = drainageService.getDrainageByDrainageId(drainageId);
        Map<String, Object> response = new HashMap<>();
        response.put("drainageId", dto.getDrainageId());
        response.put("location", dto.getLocation());
        response.put("currentWaterLevel", dto.getCurrentWaterLevel());
        response.put("capacity", dto.getCapacity());
        response.put("utilizationPercentage", dto.getUtilizationPercentage());
        response.put("status", dto.getStatus());
        return ResponseEntity.ok(response);
    }
}
