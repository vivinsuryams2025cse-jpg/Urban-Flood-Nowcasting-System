package com.flood.service;

import com.flood.dto.RainfallRequestDto;
import com.flood.dto.RainfallResponseDto;
import com.flood.entity.Rainfall;
import com.flood.exception.ResourceNotFoundException;
import com.flood.repository.RainfallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RainfallService.java
 * 
 * Service layer managing rainfall observation processing, intensity calculation,
 * database persistence, and retrieval of heavy rainfall alert zones.
 */
@Service
public class RainfallService {

    private final RainfallRepository rainfallRepository;

    @Autowired
    public RainfallService(RainfallRepository rainfallRepository) {
        this.rainfallRepository = rainfallRepository;
    }

    /**
     * Adds and persists a new rainfall observation.
     */
    public RainfallResponseDto addRainfall(RainfallRequestDto requestDto) {
        Rainfall rainfall = new Rainfall(
                requestDto.getLocation(),
                requestDto.getRainfallAmount(),
                requestDto.getDuration()
        );

        Rainfall saved = rainfallRepository.save(rainfall);
        return mapToResponseDto(saved);
    }

    /**
     * Retrieves all recorded rainfall observations.
     */
    public List<RainfallResponseDto> getAllRainfall() {
        return rainfallRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific rainfall record by ID.
     */
    public RainfallResponseDto getRainfallById(Long id) {
        Rainfall rainfall = rainfallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rainfall record", "id", id));
        return mapToResponseDto(rainfall);
    }

    /**
     * Retrieves heavy and torrential rainfall areas (intensity >= 7.6 mm/hr).
     */
    public List<RainfallResponseDto> getHeavyRainfallAreas() {
        return rainfallRepository.findByIntensityGreaterThanEqual(7.6).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds latest rainfall observation for a specific location.
     */
    public Rainfall getLatestRainfallForLocation(String location) {
        return rainfallRepository.findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(location)
                .orElse(null);
    }

    public RainfallResponseDto mapToResponseDto(Rainfall entity) {
        return new RainfallResponseDto(
                entity.getId(),
                entity.getLocation(),
                entity.getRainfallAmount(),
                entity.getDuration(),
                entity.getIntensity(),
                entity.getIntensityLevel(),
                entity.getCreatedAt()
        );
    }
}
