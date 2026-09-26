package com.flood;

/**
 * EvacuationAdvisory.java
 *
 * Day 4 Module: Evacuation & Emergency Response Advisory System
 *
 * Generates zone-specific evacuation directives, shelter assignments,
 * emergency contact escalation levels, and a time-based flood progression
 * nowcast — all derived purely from the FloodRisk assessment produced in Day 3.
 *
 * Advisory Components:
 * 1. Threat Level Classification  (CRITICAL / ELEVATED / MODERATE / NORMAL)
 * 2. Evacuation Order             (MANDATORY / RECOMMENDED / ADVISORY / NONE)
 * 3. Nearest Shelter Assignment   (assigned by zone hash for repeatability)
 * 4. Emergency Response Level     (LEVEL-1 through LEVEL-4)
 * 5. Time-Based Flood Progression (T+0 h, T+0.5 h, T+1 h, T+2 h, T+3 h)
 * 6. Actionable Instructions      (citizen & authority directives)
 */
public class EvacuationAdvisory {

    // ---------------------------------------------------------------
    // 1. Predefined Emergency Shelters (city-level static registry)
    // ---------------------------------------------------------------
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

    // ---------------------------------------------------------------
    // 2. Core Input (from FloodRisk -- Day 3)
    // ---------------------------------------------------------------
    private FloodRisk floodRisk;

    // ---------------------------------------------------------------
    // 3. Computed Advisory Outputs
    // ---------------------------------------------------------------
    private String threatLevel;        // CRITICAL / ELEVATED / MODERATE / NORMAL
    private String evacuationOrder;    // MANDATORY / RECOMMENDED / ADVISORY / NONE
    private String responseLevel;      // LEVEL-1 / LEVEL-2 / LEVEL-3 / LEVEL-4
    private String assignedShelter;    // Shelter name + capacity
    private String primaryContact;     // Primary emergency contact
    private String[] citizenSteps;     // Ordered list of citizen action steps

    /**
     * Constructor: Accepts a FloodRisk object from Day 3 and computes
     * the full evacuation advisory automatically.
     *
     * @param floodRisk The FloodRisk assessment for the zone (Day 3)
     */
    public EvacuationAdvisory(FloodRisk floodRisk) {
        this.floodRisk = floodRisk;
        generateAdvisory();
    }

    // ---------------------------------------------------------------
    // 4. Advisory Generation Engine
    // ---------------------------------------------------------------

    /**
     * Derives all advisory fields from the FloodRisk risk level.
     * Uses rule-based decision tree -- no ML required.
     */
    private void generateAdvisory() {
        String riskLevel = floodRisk.getRiskLevel();

        // Assign shelter deterministically by location name hash
        int shelterIndex = Math.abs(floodRisk.getLocation().hashCode()) % SHELTERS.length;
        this.assignedShelter = SHELTERS[shelterIndex];

        // Primary emergency contact (cycles by shelter index for variability)
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
                    this.responseLevel   = "LEVEL-3 (Precautionary Monitoring)";
                    this.citizenSteps    = new String[]{
                        "Stay alert -- conditions can change rapidly during heavy rain.",
                        "Avoid unnecessary travel especially near drainage channels.",
                        "Keep emergency kit accessible at home.",
                        "Contact " + primaryContact + " to report unusual water rise.",
                        "Designated shelter for your zone: " + assignedShelter + "."
                    };
                } else {
                    this.threatLevel     = "NORMAL";
                    this.evacuationOrder = "NO EVACUATION REQUIRED";
                    this.responseLevel   = "LEVEL-4 (Routine Operations)";
                    this.citizenSteps    = new String[]{
                        "No immediate action required. Remain aware of weather forecasts.",
                        "Drainage is operating normally -- " +
                            String.format("%.1f%% capacity utilized.", floodRisk.getCapacityUtilization()),
                        "Contact " + primaryContact + " only in case of emergency.",
                        "Nearest shelter for reference: " + assignedShelter + "."
                    };
                }
                break;
        }
    }

    // ---------------------------------------------------------------
    // 5. Time-Based Flood Progression Nowcast
    // ---------------------------------------------------------------

    /**
     * Projects the water level accumulation at future time steps:
     * T+0 h (now), T+0.5 h, T+1 h, T+2 h, T+3 h.
     *
     * Progression model:
     *   Projected Load(t) = Current Water Level + (Rainfall Intensity * t)
     *   Utilization(t)    = Projected Load(t) / Drainage Capacity * 100%
     *
     * This is a simplified linear accumulation model -- suitable for
     * short-term (nowcasting) horizons without statistical libraries.
     */
    public void displayFloodProgression() {
        double intensity      = floodRisk.getRainfallIntensity();
        double capacity       = floodRisk.getDrainageCapacity();
        double baseWaterLevel = floodRisk.getCurrentWaterLevel();

        double[] timeSteps = { 0.0, 0.5, 1.0, 2.0, 3.0 };

        System.out.println("\n---------- TIME-BASED FLOOD PROGRESSION NOWCAST -----------");
        System.out.printf("%-10s %-22s %-22s %-16s%n",
                "Time (h)", "Projected Load (mm)", "Utilization (%)", "Status");
        System.out.println("-----------------------------------------------------------");

        for (double t : timeSteps) {
            double projectedLoad = baseWaterLevel + (intensity * t);
            double utilization   = (capacity > 0) ? (projectedLoad / capacity) * 100.0 : 100.0;
            String status;

            if (projectedLoad > capacity) {
                status = "*** OVERFLOW ***";
            } else if (utilization >= 85.0) {
                status = "CRITICAL";
            } else if (utilization >= 65.0) {
                status = "ELEVATED";
            } else if (utilization >= 40.0) {
                status = "MODERATE";
            } else {
                status = "NORMAL";
            }

            System.out.printf("T+%-8.1f %-22.2f %-22.1f %-16s%n",
                    t, projectedLoad, Math.min(utilization, 999.9), status);
        }
        System.out.println("-----------------------------------------------------------");
        System.out.println("Note: Linear accumulation model at constant observed intensity.");
    }

    // ---------------------------------------------------------------
    // 6. Display Methods
    // ---------------------------------------------------------------

    /**
     * Displays the complete Evacuation & Emergency Response Advisory Report.
     */
    public void displayAdvisoryReport() {
        System.out.println("\n===========================================================");
        System.out.println("      EVACUATION & EMERGENCY RESPONSE ADVISORY REPORT      ");
        System.out.println("===========================================================");
        System.out.println("Zone / Location     : " + floodRisk.getLocation());
        System.out.printf("Flood Risk Level    : [%s]%n", floodRisk.getRiskLevel());
        System.out.printf("Capacity Utilized   : %.1f%% (Total Load: %.2f mm / %.2f mm)%n",
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
            System.out.printf("  %d. %s%n", i + 1, citizenSteps[i]);
        }

        // Flood Progression Table
        displayFloodProgression();

        System.out.println("\nADDITIONAL EMERGENCY CONTACTS:");
        for (String contact : EMERGENCY_CONTACTS) {
            System.out.println("  - " + contact);
        }
        System.out.println("===========================================================");
    }

    // ---------------------------------------------------------------
    // 7. Getters
    // ---------------------------------------------------------------

    public String getThreatLevel() {
        return threatLevel;
    }

    public String getEvacuationOrder() {
        return evacuationOrder;
    }

    public String getResponseLevel() {
        return responseLevel;
    }

    public String getAssignedShelter() {
        return assignedShelter;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public String[] getCitizenSteps() {
        return citizenSteps;
    }

    public FloodRisk getFloodRisk() {
        return floodRisk;
    }
}
