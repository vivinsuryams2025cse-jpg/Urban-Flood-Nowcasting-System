package com.flood.service;

import com.flood.dto.FloodRiskResponseDto;
import com.flood.entity.Drainage;
import com.flood.entity.FloodRisk;
import com.flood.entity.FloodZone;
import com.flood.entity.Rainfall;
import com.flood.repository.DrainageRepository;
import com.flood.repository.FloodRiskRepository;
import com.flood.repository.RainfallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FloodRiskService.java
 * 
 * Core hydrological service computing coupled flood risk (LOW, MEDIUM, HIGH)
 * and flood zone classification (SAFE, WARNING, DANGER).
 */
@Service
public class FloodRiskService {

    private final FloodRiskRepository floodRiskRepository;
    private final DrainageRepository drainageRepository;
    private final RainfallRepository rainfallRepository;

    @Autowired
    public FloodRiskService(FloodRiskRepository floodRiskRepository,
                            DrainageRepository drainageRepository,
                            RainfallRepository rainfallRepository) {
        this.floodRiskRepository = floodRiskRepository;
        this.drainageRepository = drainageRepository;
        this.rainfallRepository = rainfallRepository;
    }

    /**
     * Calculates coupled flood risk for a specific registered drainage unit.
     */
    public FloodRiskResponseDto calculateRiskForDrainage(String drainageId) {
        Drainage drainage = drainageRepository.findByDrainageIdIgnoreCase(drainageId)
                .orElseThrow(() -> new IllegalArgumentException("Drainage not found with ID: " + drainageId));

        Rainfall rainfall = rainfallRepository.findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(drainage.getLocation())
                .orElse(null);

        FloodRisk risk = new FloodRisk(rainfall, drainage);
        FloodRisk savedRisk = floodRiskRepository.save(risk);

        FloodZone zone = new FloodZone(drainage, rainfall);
        return mapToResponseDto(savedRisk, zone);
    }

    /**
     * Calculates flood risk from custom hydrological simulation inputs.
     */
    public FloodRiskResponseDto calculateCustomRisk(String location, double intensity, 
                                                    double duration, double capacity, 
                                                    double waterLevel) {
        FloodRisk risk = new FloodRisk(location, intensity, duration, capacity, waterLevel);
        FloodRisk savedRisk = floodRiskRepository.save(risk);

        FloodZone zone = new FloodZone(location, intensity, duration, capacity, waterLevel);
        return mapToResponseDto(savedRisk, zone);
    }

    /**
     * Classifies an area into SAFE, WARNING, or DANGER flood zones (Day 5).
     */
    public FloodZone checkFloodZone(String drainageId) {
        Drainage drainage = drainageRepository.findByDrainageIdIgnoreCase(drainageId)
                .orElseThrow(() -> new IllegalArgumentException("Drainage not found with ID: " + drainageId));

        Rainfall rainfall = rainfallRepository.findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(drainage.getLocation())
                .orElse(null);

        return new FloodZone(drainage, rainfall);
    }

    /**
     * Retrieves historical flood risk assessments.
     */
    public List<FloodRiskResponseDto> getFloodRiskHistory() {
        return floodRiskRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(risk -> {
                    FloodZone zone = new FloodZone(risk);
                    return mapToResponseDto(risk, zone);
                })
                .collect(Collectors.toList());
    }

    public FloodRiskResponseDto mapToResponseDto(FloodRisk risk, FloodZone zone) {
        double incomingVolume = risk.getRainfallIntensity() * risk.getRainfallDuration();
        String zoneClass = (zone != null) ? zone.getZoneClassification() : 
                ("HIGH".equalsIgnoreCase(risk.getRiskLevel()) ? "DANGER" : 
                ("MEDIUM".equalsIgnoreCase(risk.getRiskLevel()) ? "WARNING" : "SAFE"));
        String zoneColor = (zone != null) ? zone.getColorIndicator() :
                ("HIGH".equalsIgnoreCase(risk.getRiskLevel()) ? "RED" : 
                ("MEDIUM".equalsIgnoreCase(risk.getRiskLevel()) ? "AMBER / YELLOW" : "GREEN"));

        return new FloodRiskResponseDto(
                risk.getId(),
                risk.getLocation(),
                risk.getRainfallIntensity(),
                risk.getRainfallDuration(),
                risk.getDrainageCapacity(),
                risk.getCurrentWaterLevel(),
                incomingVolume,
                risk.getTotalWaterLoad(),
                Math.round(risk.getCapacityUtilization() * 10.0) / 10.0,
                risk.getRiskLevel(),
                risk.getRiskReason(),
                risk.getWarningMessage(),
                zoneClass,
                zoneColor,
                risk.getCreatedAt()
        );
    }
}
