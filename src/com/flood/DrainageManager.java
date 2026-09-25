package com.flood;

import java.util.ArrayList;

/**
 * DrainageManager.java
 * 
 * Manages the collection of urban drainage systems.
 * Uses an ArrayList to store Drainage objects and provides methods to:
 * - Add a new drainage record
 * - Display all drainage records
 * - Filter and display drainage systems at WARNING or OVERFLOW RISK
 * - Find a drainage unit by its ID or location
 * - Connect and couple drainage records with rainfall data from Day 1
 */
public class DrainageManager {
    // List to hold multiple Drainage objects in memory
    private ArrayList<Drainage> drainageList;

    /**
     * Constructor initializes an empty ArrayList.
     */
    public DrainageManager() {
        drainageList = new ArrayList<>();
    }

    /**
     * Adds a new Drainage record to the list.
     * 
     * @param drainage The Drainage object to add
     */
    public void addDrainage(Drainage drainage) {
        drainageList.add(drainage);
        System.out.println("\n[SUCCESS] Drainage system registered successfully! ID: " 
                + drainage.getDrainageId() + " (" + drainage.getLocation() + ")");
    }

    /**
     * Displays all registered drainage systems in a clean, numbered list.
     */
    public void displayAllDrainages() {
        if (drainageList.isEmpty()) {
            System.out.println("\n[INFO] No drainage systems found. Please add a drainage system first.");
            return;
        }

        System.out.println("\n==================================================");
        System.out.println("             ALL DRAINAGE SYSTEM RECORDS          ");
        System.out.println("==================================================");

        for (int i = 0; i < drainageList.size(); i++) {
            System.out.println("Drainage System #" + (i + 1));
            drainageList.get(i).displayDetails();
        }
        System.out.println("Total Drainage Systems: " + drainageList.size());
    }

    /**
     * Filters and displays drainage systems currently facing WARNING or OVERFLOW RISK.
     */
    public void displayOverflowRiskDrainages() {
        if (drainageList.isEmpty()) {
            System.out.println("\n[INFO] No drainage systems available to evaluate.");
            return;
        }

        boolean found = false;
        System.out.println("\n==================================================");
        System.out.println("      CRITICAL DRAINAGE ALERT (WARNING / OVERFLOW) ");
        System.out.println("==================================================");

        for (int i = 0; i < drainageList.size(); i++) {
            Drainage drain = drainageList.get(i);
            if (drain.getStatus().equalsIgnoreCase("OVERFLOW RISK") 
                    || drain.getStatus().equalsIgnoreCase("WARNING")) {
                drain.displayDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println("[INFO] All drainage systems are operating normally. No overflow risks detected.");
        }
    }

    /**
     * Finds a Drainage unit by its unique Drainage ID (case-insensitive).
     * 
     * @param drainageId The ID to search for
     * @return Drainage object if found, null otherwise
     */
    public Drainage findDrainageById(String drainageId) {
        for (Drainage drain : drainageList) {
            if (drain.getDrainageId().equalsIgnoreCase(drainageId.trim())) {
                return drain;
            }
        }
        return null;
    }

    /**
     * Finds a Drainage unit by its Location name (case-insensitive partial match or exact).
     * 
     * @param location Location name to search for
     * @return Drainage object if found, null otherwise
     */
    public Drainage findDrainageByLocation(String location) {
        for (Drainage drain : drainageList) {
            if (drain.getLocation().equalsIgnoreCase(location.trim())) {
                return drain;
            }
        }
        return null;
    }

    /**
     * Connects and couples each drainage system with matching rainfall data from Day 1.
     * Evaluates whether each drainage system can handle the observed rainfall at that location.
     * 
     * @param rainfallManager The RainfallManager holding Day 1 rainfall observations
     */
    public void coupleWithRainfallData(RainfallManager rainfallManager) {
        if (drainageList.isEmpty()) {
            System.out.println("\n[INFO] No drainage systems registered. Please add drainage records first.");
            return;
        }

        System.out.println("\n=============================================================");
        System.out.println("   CITY-WIDE DRAINAGE & RAINFALL COUPLING NOWCAST ANALYSIS   ");
        System.out.println("=============================================================");

        boolean anyMatched = false;

        for (Drainage drain : drainageList) {
            Rainfall matchedRainfall = rainfallManager.findRainfallByLocation(drain.getLocation());
            if (matchedRainfall != null) {
                anyMatched = true;
                System.out.println("\n[MATCHED ZONE] " + drain.getLocation());
                System.out.printf("Rainfall Intensity Observed: %.2f mm/hr (%s)\n", 
                        matchedRainfall.getIntensity(), matchedRainfall.getIntensityLevel());
                drain.displayCoupledAnalysis(matchedRainfall.getRainfallAmount());
            } else {
                System.out.println("\n[INFO] Drainage ID " + drain.getDrainageId() + " (" + drain.getLocation() 
                        + "): No rainfall sensor data coupled yet. Operating at baseline status: " + drain.getStatus());
            }
        }

        if (!anyMatched) {
            System.out.println("\n[TIP] No direct location match found between rainfall and drainage records.");
            System.out.println("Add rainfall records with matching location names to see automated coupling.");
        }
    }

    /**
     * Evaluates city-wide flood risk across all registered drainage systems
     * by coupling each system with its corresponding rainfall record using the FloodRisk class (Day 3).
     * 
     * @param rainfallManager The RainfallManager holding Day 1 rainfall observations
     */
    public void evaluateFloodRisks(RainfallManager rainfallManager) {
        if (drainageList.isEmpty()) {
            System.out.println("\n[INFO] No drainage systems registered. Please add drainage records first.");
            return;
        }

        System.out.println("\n=============================================================");
        System.out.println("     CITY-WIDE FLOOD RISK ASSESSMENT (DAY 3 NOWCASTING)      ");
        System.out.println("=============================================================");

        int lowCount = 0;
        int medCount = 0;
        int highCount = 0;
        int uncoupledCount = 0;

        for (Drainage drain : drainageList) {
            Rainfall matchedRainfall = rainfallManager.findRainfallByLocation(drain.getLocation());
            if (matchedRainfall != null) {
                FloodRisk risk = new FloodRisk(matchedRainfall, drain);
                risk.displayRiskReport();

                if ("HIGH".equalsIgnoreCase(risk.getRiskLevel())) {
                    highCount++;
                } else if ("MEDIUM".equalsIgnoreCase(risk.getRiskLevel())) {
                    medCount++;
                } else {
                    lowCount++;
                }
            } else {
                uncoupledCount++;
                System.out.println("\n[NOTICE] Drainage ID: " + drain.getDrainageId() + " (" + drain.getLocation() 
                        + "): No rainfall sensor data coupled yet. Cannot compute full flood risk.");
            }
        }

        System.out.println("\n----------------- FLOOD RISK SUMMARY REPORT -----------------");
        System.out.println("  HIGH RISK ZONES   : " + highCount + " (CRITICAL ALERT)");
        System.out.println("  MEDIUM RISK ZONES : " + medCount + " (ADVISORY MONITORING)");
        System.out.println("  LOW RISK ZONES    : " + lowCount + " (SAFE CONDITIONS)");
        if (uncoupledCount > 0) {
            System.out.println("  UNCOUPLED ZONES   : " + uncoupledCount + " (Pending rainfall data)");
        }
        System.out.println("=============================================================");
    }

    /**
     * Calculates flood risk for a specific drainage ID by coupling with rainfall data.
     * 
     * @param drainageId Drainage system ID to evaluate
     * @param rainfallManager RainfallManager to look up matching rainfall record
     * @return FloodRisk object or null if drainage not found
     */
    public FloodRisk calculateRiskForDrainage(String drainageId, RainfallManager rainfallManager) {
        Drainage drain = findDrainageById(drainageId);
        if (drain == null) {
            return null;
        }
        Rainfall rain = rainfallManager.findRainfallByLocation(drain.getLocation());
        return new FloodRisk(rain, drain);
    }

    /**
     * Returns the list of drainage records.
     */
    public ArrayList<Drainage> getDrainageList() {
        return drainageList;
    }

    /**
     * Returns the total count of registered drainage systems.
     */
    public int getDrainageCount() {
        return drainageList.size();
    }
}

