package com.flood.service;

import com.flood.entity.Drainage;
import com.flood.entity.FloodAlert;
import com.flood.entity.FloodRisk;
import com.flood.entity.Rainfall;
import com.flood.repository.DrainageRepository;
import com.flood.repository.FloodAlertRepository;
import com.flood.repository.RainfallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AlertService.java
 * 
 * Generates emergency civil defense alerts, shelter assignments, and evacuation directives
 * based on coupled flood risk evaluations.
 */
@Service
public class AlertService {

    private final FloodAlertRepository floodAlertRepository;
    private final DrainageRepository drainageRepository;
    private final RainfallRepository rainfallRepository;

    private static final String[] SHELTERS = {
        "Municipal Stadium (Capacity: 5,000) -- Zone A",
        "City Community Hall (Capacity: 2,000) -- Zone B",
        "Central High School Campus (Capacity: 3,500) -- Zone C",
        "District Sports Complex (Capacity: 4,000) -- Zone D",
        "Railway Station Relief Camp (Capacity: 2,500) -- Zone E"
    };

    private static final String[] EMERGENCY_CONTACTS = {
        "City Flood Control Room: 1800-XXX-001",
        "Fire & Rescue Services: 101",
        "Medical Emergency: 108",
        "Police Emergency: 100",
        "National Disaster Helpline: 1078"
    };

    @Autowired
    public AlertService(FloodAlertRepository floodAlertRepository,
                        DrainageRepository drainageRepository,
                        RainfallRepository rainfallRepository) {
        this.floodAlertRepository = floodAlertRepository;
        this.drainageRepository = drainageRepository;
        this.rainfallRepository = rainfallRepository;
    }

    /**
     * Generates and persists an emergency flood alert for a specific drainage zone.
     */
    public FloodAlert generateAlertForDrainage(String drainageId) {
        Drainage drainage = drainageRepository.findByDrainageIdIgnoreCase(drainageId)
                .orElseThrow(() -> new IllegalArgumentException("Drainage not found with ID: " + drainageId));

        Rainfall rainfall = rainfallRepository.findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(drainage.getLocation())
                .orElse(null);

        FloodRisk risk = new FloodRisk(rainfall, drainage);

        int shelterIndex = Math.abs(drainage.getLocation().hashCode()) % SHELTERS.length;
        String assignedShelter = SHELTERS[shelterIndex];
        String primaryContact = EMERGENCY_CONTACTS[shelterIndex % EMERGENCY_CONTACTS.length];

        String threatLevel;
        String evacuationOrder;
        String responseLevel;
        String message;

        switch (risk.getRiskLevel().toUpperCase()) {
            case "HIGH":
                threatLevel = "CRITICAL";
                evacuationOrder = "MANDATORY EVACUATION";
                responseLevel = "LEVEL-1 (Maximum Emergency Response)";
                message = "CRITICAL FLOOD HAZARD: Immediate evacuation ordered. Move to " + assignedShelter + ". Call " + primaryContact + " for rescue assistance.";
                break;
            case "MEDIUM":
                threatLevel = "ELEVATED";
                evacuationOrder = "RECOMMENDED EVACUATION";
                responseLevel = "LEVEL-2 (Enhanced Readiness)";
                message = "ELEVATED FLOOD WARNING: Prepare emergency kit and be ready to move to " + assignedShelter + ". Avoid low-lying underpasses.";
                break;
            default:
                threatLevel = "NORMAL";
                evacuationOrder = "NONE";
                responseLevel = "LEVEL-4 (Routine Monitoring)";
                message = "SAFE HYDROLOGICAL CONDITION: Normal drainage flow. No evacuation required.";
                break;
        }

        FloodAlert alert = new FloodAlert(
                drainage.getLocation(),
                threatLevel,
                evacuationOrder,
                responseLevel,
                assignedShelter,
                primaryContact,
                message
        );

        return floodAlertRepository.save(alert);
    }

    /**
     * Retrieves all issued alerts in reverse chronological order.
     */
    public List<FloodAlert> getAllAlerts() {
        return floodAlertRepository.findAllByOrderByIssuedAtDesc();
    }

    /**
     * Filters alerts by threat level (e.g. CRITICAL, ELEVATED).
     */
    public List<FloodAlert> getAlertsByThreatLevel(String threatLevel) {
        return floodAlertRepository.findByThreatLevelIgnoreCase(threatLevel);
    }
}
