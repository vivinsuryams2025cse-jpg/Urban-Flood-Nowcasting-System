package com.flood.controller;

import com.flood.dto.RainfallRequestDto;
import com.flood.dto.RainfallResponseDto;
import com.flood.service.RainfallService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RainfallController.java
 * 
 * REST controller for rainfall observation endpoints.
 * Provides APIs to record sensor observations, retrieve records, and view high-intensity zones.
 */
@RestController
@RequestMapping("/api/rainfall")
public class RainfallController {

    private final RainfallService rainfallService;

    @Autowired
    public RainfallController(RainfallService rainfallService) {
        this.rainfallService = rainfallService;
    }

    /**
     * POST /api/rainfall
     * Adds and registers a new rainfall record.
     */
    @PostMapping
    public ResponseEntity<RainfallResponseDto> addRainfall(@Valid @RequestBody RainfallRequestDto requestDto) {
        RainfallResponseDto responseDto = rainfallService.addRainfall(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * GET /api/rainfall
     * Retrieves all recorded rainfall observations.
     */
    @GetMapping
    public ResponseEntity<List<RainfallResponseDto>> getAllRainfall() {
        return ResponseEntity.ok(rainfallService.getAllRainfall());
    }

    /**
     * GET /api/rainfall/{id}
     * Retrieves a rainfall record by database ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RainfallResponseDto> getRainfallById(@PathVariable Long id) {
        return ResponseEntity.ok(rainfallService.getRainfallById(id));
    }

    /**
     * GET /api/rainfall/heavy
     * Retrieves high-risk / heavy rainfall zones (>= 7.6 mm/hr).
     */
    @GetMapping("/heavy")
    public ResponseEntity<List<RainfallResponseDto>> getHeavyRainfallZones() {
        return ResponseEntity.ok(rainfallService.getHeavyRainfallAreas());
    }
}
