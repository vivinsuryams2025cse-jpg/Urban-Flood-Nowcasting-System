package com.flood.service;

import com.flood.dto.FloodRiskResponseDto;
import com.flood.entity.Drainage;
import com.flood.entity.FloodRisk;
import com.flood.entity.FloodZone;
import com.flood.entity.Rainfall;
import com.flood.repository.DrainageRepository;
import com.flood.repository.RainfallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ReportService.java
 * 
 * Generates city-wide flood nowcasting reports, summary statistics,
 * threat counts, and coupled zone assessments across all registered catchments.
 */
@Service
public class ReportService {

    private final DrainageRepository drainageRepository;
    private final RainfallRepository rainfallRepository;
    private final FloodRiskService floodRiskService;

    @Autowired
    public ReportService(DrainageRepository drainageRepository,
                         RainfallRepository rainfallRepository,
                         FloodRiskService floodRiskService) {
        this.drainageRepository = drainageRepository;
        this.rainfallRepository = rainfallRepository;
        this.floodRiskService = floodRiskService;
    }

    /**
     * Generates a comprehensive city-wide flood nowcasting report.
     */
    public Map<String, Object> generateCityWideReport() {
        List<Drainage> drainages = drainageRepository.findAll();
        List<FloodRiskResponseDto> zoneReports = new ArrayList<>();

        int highRiskCount = 0;
        int mediumRiskCount = 0;
        int lowRiskCount = 0;

        int safeZoneCount = 0;
        int warningZoneCount = 0;
        int dangerZoneCount = 0;

        for (Drainage drainage : drainages) {
            Rainfall rainfall = rainfallRepository.findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(drainage.getLocation())
                    .orElse(null);

            FloodRisk risk = new FloodRisk(rainfall, drainage);
            FloodZone zone = new FloodZone(drainage, rainfall);
            FloodRiskResponseDto dto = floodRiskService.mapToResponseDto(risk, zone);
            zoneReports.add(dto);

            if ("HIGH".equalsIgnoreCase(risk.getRiskLevel())) {
                highRiskCount++;
            } else if ("MEDIUM".equalsIgnoreCase(risk.getRiskLevel())) {
                mediumRiskCount++;
            } else {
                lowRiskCount++;
            }

            if ("DANGER".equalsIgnoreCase(zone.getZoneClassification())) {
                dangerZoneCount++;
            } else if ("WARNING".equalsIgnoreCase(zone.getZoneClassification())) {
                warningZoneCount++;
            } else {
                safeZoneCount++;
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalMonitoredZones", drainages.size());
        summary.put("highRiskZones", highRiskCount);
        summary.put("mediumRiskZones", mediumRiskCount);
        summary.put("lowRiskZones", lowRiskCount);
        summary.put("dangerZones", dangerZoneCount);
        summary.put("warningZones", warningZoneCount);
        summary.put("safeZones", safeZoneCount);

        Map<String, Object> fullReport = new HashMap<>();
        fullReport.put("reportTitle", "City-Wide Urban Flood Nowcasting Assessment");
        fullReport.put("generatedAt", LocalDateTime.now());
        fullReport.put("summary", summary);
        fullReport.put("zones", zoneReports);

        return fullReport;
    }
}
