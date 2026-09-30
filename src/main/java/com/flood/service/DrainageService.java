package com.flood.service;

import com.flood.dto.DrainageRequestDto;
import com.flood.dto.DrainageResponseDto;
import com.flood.entity.Drainage;
import com.flood.exception.ResourceNotFoundException;
import com.flood.repository.DrainageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DrainageService.java
 * 
 * Service layer managing urban drainage system registration, capacity utilization monitoring,
 * real-time water level adjustments, and overflow vulnerability alerts.
 */
@Service
public class DrainageService {

    private final DrainageRepository drainageRepository;

    @Autowired
    public DrainageService(DrainageRepository drainageRepository) {
        this.drainageRepository = drainageRepository;
    }

    /**
     * Adds and registers a new urban drainage unit.
     */
    public DrainageResponseDto addDrainage(DrainageRequestDto requestDto) {
        if (drainageRepository.existsByDrainageIdIgnoreCase(requestDto.getDrainageId())) {
            throw new IllegalArgumentException("Drainage system with ID '" + requestDto.getDrainageId() + "' already exists!");
        }

        Drainage drainage = new Drainage(
                requestDto.getDrainageId(),
                requestDto.getLocation(),
                requestDto.getCapacity(),
                requestDto.getCurrentWaterLevel()
        );

        Drainage saved = drainageRepository.save(drainage);
        return mapToResponseDto(saved);
    }

    /**
     * Retrieves all registered drainage systems.
     */
    public List<DrainageResponseDto> getAllDrainages() {
        return drainageRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves drainage by primary database ID.
     */
    public DrainageResponseDto getDrainageById(Long id) {
        Drainage drainage = drainageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drainage system", "id", id));
        return mapToResponseDto(drainage);
    }

    /**
     * Retrieves drainage by business identifier (e.g., DRN-101).
     */
    public Drainage getDrainageEntityByDrainageId(String drainageId) {
        return drainageRepository.findByDrainageIdIgnoreCase(drainageId)
                .orElseThrow(() -> new ResourceNotFoundException("Drainage system", "drainageId", drainageId));
    }

    /**
     * Retrieves drainage DTO by business identifier.
     */
    public DrainageResponseDto getDrainageByDrainageId(String drainageId) {
        Drainage drainage = getDrainageEntityByDrainageId(drainageId);
        return mapToResponseDto(drainage);
    }

    /**
     * Updates and monitors current water level of a drainage system.
     */
    public DrainageResponseDto updateWaterLevel(String drainageId, double newWaterLevel) {
        if (newWaterLevel < 0) {
            throw new IllegalArgumentException("Water level cannot be negative!");
        }

        Drainage drainage = getDrainageEntityByDrainageId(drainageId);
        drainage.setCurrentWaterLevel(newWaterLevel);
        Drainage updated = drainageRepository.save(drainage);
        return mapToResponseDto(updated);
    }

    /**
     * Filters systems facing WARNING or OVERFLOW RISK.
     */
    public List<DrainageResponseDto> getOverflowRiskDrainages() {
        return drainageRepository.findByStatusIn(Arrays.asList("WARNING", "OVERFLOW RISK")).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public DrainageResponseDto mapToResponseDto(Drainage entity) {
        double utilization = (entity.getCapacity() > 0)
                ? (entity.getCurrentWaterLevel() / entity.getCapacity()) * 100.0
                : 100.0;

        return new DrainageResponseDto(
                entity.getId(),
                entity.getDrainageId(),
                entity.getLocation(),
                entity.getCapacity(),
                entity.getCurrentWaterLevel(),
                Math.round(utilization * 10.0) / 10.0,
                entity.getStatus()
        );
    }
}
