package com.flood.service;

import com.flood.entity.FloodRisk;
import org.springframework.stereotype.Service;

/**
 * EvacuationAdvisory.java
 *
 * Preserved Day 4 Module: Evacuation & Emergency Response Advisory System.
 * Generates zone-specific evacuation directives, shelter assignments,
 * emergency contact escalation levels, and a time-based flood progression
 * nowcast derived from FloodRisk assessment.
 */
public class EvacuationAdvisory {

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

    private FloodRisk floodRisk;
    private String threatLevel;        // CRITICAL / ELEVATED / MODERATE / NORMAL
    private String evacuationOrder;    // MANDATORY / RECOMMENDED / ADVISORY / NONE
    private String responseLevel;      // LEVEL-1 / LEVEL-2 / LEVEL-3 / LEVEL-4
    private String assignedShelter;    // Shelter name + capacity
    private String primaryContact;     // Primary emergency contact
    private String[] citizenSteps;     // Ordered list of citizen action steps

    public EvacuationAdvisory(FloodRisk floodRisk) {
        this.floodRisk = floodRisk;
        generateAdvisory();
    }

    private void generateAdvisory() {
        String riskLevel = floodRisk.getRiskLevel();

        int shelterIndex = Math.abs(floodRisk.getLocation().hashCode()) % SHELTERS.length;
        this.assignedShelter = SHELTERS[shelterIndex];
        this.primaryContact = EMERGENCY_CONTACTS[shelterIndex % EMERGENCY_CONTACTS.length];

        switch (riskLevel.toUpperCase()) {
            case "HIGH":
                this.threatLevel     = "CRITICAL";
                this.evacuationOrder = "MANDATORY EVACUATION";
                this.responseLevel   = "LEVEL-1 (Maximum Emergency Response)";
                this.citizenSteps    = new String[]{
                    "EVACUATE IMMEDIATELY -- do not wait for water to rise.",
                    "Move to assigned shelter: " + assignedShelter + ".",
                    "Do NOT enter flooded roads or underpasses -- turn around, don't drown.",
                    "Switch off all electrical mains before leaving home.",
                    "Take emergency kit: documents, medicines, drinking water, phone charger.",
                    "Call " + primaryContact + " if trapped or requiring rescue.",
                    "Stay tuned to official broadcast channels for real-time updates."
                };
                break;

            case "MEDIUM":
                this.threatLevel     = "ELEVATED";
                this.evacuationOrder = "RECOMMENDED EVACUATION";
                this.responseLevel   = "LEVEL-2 (Enhanced Readiness)";
                this.citizenSteps    = new String[]{
                    "Prepare evacuation bag -- be ready to leave within 30 minutes.",
                    "Identify nearest shelter: " + assignedShelter + ".",
                    "Avoid low-lying areas, subways, and underpasses.",
                    "Check on elderly neighbours and those with mobility constraints.",
                    "Move vehicles to higher ground to prevent damage.",
                    "Monitor water levels every 15 minutes -- evacuate immediately if rising.",
                    "Contact: " + primaryContact + " for updates."
                };
                break;

            case "LOW":
            default:
                if (floodRisk.getCapacityUtilization() >= 40.0) {
                    this.threatLevel     = "MODERATE";
                    this.evacuationOrder = "PRECAUTIONARY ADVISORY";
                    this.responseLevel   = "LEVEL-3 (Heightened Vigilance)";
                    this.citizenSteps    = new String[]{
                        "Stay alert -- rainfall is manageable but runoff is increasing.",
                        "Clear household drains and storm gutters of debris.",
                        "Charge mobile devices and emergency battery banks.",
                        "Avoid parking over stormwater grates or near open culverts.",
                        "Check local traffic alerts before travelling."
                    };
                } else {
                    this.threatLevel     = "NORMAL";
                    this.evacuationOrder = "NO EVACUATION REQUIRED";
                    this.responseLevel   = "LEVEL-4 (Routine Monitoring)";
                    this.citizenSteps    = new String[]{
                        "Normal conditions prevail -- no immediate flood danger.",
                        "Maintain standard monsoon precautions.",
                        "Keep emergency helpline numbers saved: " + primaryContact + "."
                    };
                }
                break;
        }
    }

    public void displayAdvisoryReport() {
        System.out.println("===========================================================");
        System.out.println("      EVACUATION & EMERGENCY RESPONSE ADVISORY REPORT      ");
        System.out.println("===========================================================");
        System.out.println("Zone / Location     : " + floodRisk.getLocation());
        System.out.println("Flood Risk Level    : [" + floodRisk.getRiskLevel() + "]");
        System.out.printf ("Capacity Utilized   : %.1f%% (Total Load: %.2f mm / %.2f mm)\n",
                floodRisk.getCapacityUtilization(),
                floodRisk.getTotalWaterLoad(),
                floodRisk.getDrainageCapacity());
        System.out.println("-----------------------------------------------------------");
        System.out.println("THREAT LEVEL        : " + threatLevel);
        System.out.println("EVACUATION ORDER    : " + evacuationOrder);
        System.out.println("RESPONSE LEVEL      : " + responseLevel);
        System.out.println("ASSIGNED SHELTER    : " + assignedShelter);
        System.out.println("PRIMARY CONTACT     : " + primaryContact);
        System.out.println("-----------------------------------------------------------");
        System.out.println("CITIZEN ACTION STEPS:");
        for (int i = 0; i < citizenSteps.length; i++) {
            System.out.printf("  %d. %s\n", (i + 1), citizenSteps[i]);
        }
        System.out.println("===========================================================");
    }

    public String getThreatLevel() { return threatLevel; }
    public String getEvacuationOrder() { return evacuationOrder; }
    public String getResponseLevel() { return responseLevel; }
    public String getAssignedShelter() { return assignedShelter; }
    public String getPrimaryContact() { return primaryContact; }
    public String[] getCitizenSteps() { return citizenSteps; }
    public FloodRisk getFloodRisk() { return floodRisk; }
}
