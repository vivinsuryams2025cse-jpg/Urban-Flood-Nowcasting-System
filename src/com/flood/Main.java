package com.flood;

import java.util.Scanner;

/**
 * Main.java
 * 
 * Main entry point for the Urban Flood Nowcasting System.
 * Integrates:
 * - Day 1: Rainfall Observation Module
 * - Day 2: Drainage System & Rainfall-Drainage Coupling Module
 * - Day 3: Flood Risk Calculation & Warning Module (Rule-Based Evaluation)
 * - Day 4: Evacuation & Emergency Response Advisory System
 * 
 * Presents an interactive text-based console menu for users to:
 * - Enter and view rainfall observation data (Day 1)
 * - Enter and view urban drainage systems (Day 2)
 * - Calculate whether drainage systems can handle rainfall (Day 2)
 * - Run city-wide rainfall-drainage coupling nowcast (Day 2)
 * - Calculate rule-based flood risk (LOW, MEDIUM, HIGH) (Day 3)
 * - Run city-wide flood risk assessments with warnings and reasons (Day 3)
 * - Perform custom 4-parameter flood risk simulations (Day 3)
 * - Generate evacuation advisories with shelter assignment and flood progression (Day 4)
 * - Run city-wide evacuation advisory sweep (Day 4)
 * - Generate custom evacuation advisory via manual input (Day 4)
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RainfallManager rainfallManager = new RainfallManager();
        DrainageManager drainageManager = new DrainageManager();

        // -------------------------------------------------------------
        // INITIAL SAMPLE DATA (Day 1 Rainfall & Day 2 Drainage)
        // Demonstrates all 3 Day 3 Risk Levels (LOW, MEDIUM, HIGH)
        // -------------------------------------------------------------
        
        // Day 1 Sample Rainfall Records
        rainfallManager.addRainfall(new Rainfall("Sector 4 Central Canal", 45.0, 1.5));
        rainfallManager.addRainfall(new Rainfall("Downtown Metro Subway Drain", 85.0, 1.0));
        rainfallManager.addRainfall(new Rainfall("Riverside Boulevard Culvert", 10.0, 2.0));

        // Day 2 Sample Drainage Records (Coupled with the above zones)
        drainageManager.addDrainage(new Drainage("DRN-101", "Sector 4 Central Canal", 100.0, 30.0));
        drainageManager.addDrainage(new Drainage("DRN-102", "Downtown Metro Subway Drain", 80.0, 25.0));
        drainageManager.addDrainage(new Drainage("DRN-103", "Riverside Boulevard Culvert", 120.0, 15.0));

        boolean running = true;

        System.out.println("=============================================================");
        System.out.println("  URBAN FLOOD NOWCASTING SYSTEM (Drainage & Rainfall Coupling)");
        System.out.println("  [DAY 4: Evacuation & Emergency Response Advisory Module]" );
        System.out.println("=============================================================");

        while (running) {
            // Display Console Menu
            System.out.println("\n----------------------- MAIN MENU -----------------------");
            System.out.println("  [Rainfall Observation - Day 1]");
            System.out.println("  1. Add New Rainfall Record");
            System.out.println("  2. Display All Rainfall Records");
            System.out.println("  3. Display High-Risk / Heavy Rainfall Zones");
            System.out.println("  [Drainage System & Coupling - Day 2]");
            System.out.println("  4. Add New Drainage System");
            System.out.println("  5. Display All Drainage Systems");
            System.out.println("  6. Check if Drainage Can Handle Rainfall (Coupled Analysis)");
            System.out.println("  7. Run City-Wide Rainfall & Drainage Coupling Nowcast");
            System.out.println("  8. Display Drainage Systems at Overflow Risk / Warning");
            System.out.println("  [Flood Risk Calculation - Day 3]");
            System.out.println("  9. Calculate Flood Risk for a Drainage Zone (Coupled)");
            System.out.println("  10. Run City-Wide Flood Risk Assessment (All Zones)");
            System.out.println("  11. Calculate Custom Flood Risk (Manual 4-Variable Input)");
            System.out.println("  [Evacuation & Emergency Advisory - Day 4]");
            System.out.println("  13. Generate Evacuation Advisory for a Drainage Zone");
            System.out.println("  14. Run City-Wide Evacuation Advisory Sweep (All Zones)");
            System.out.println("  15. Generate Custom Evacuation Advisory (Manual Input)");
            System.out.println("  [System]");
            System.out.println("  16. Exit System");
            System.out.print("Please enter your choice (1-16): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Option 1: Add new rainfall record (Day 1)
                    System.out.println("\n--- [ENTER RAINFALL DATA] ---");
                    
                    System.out.print("Enter Location Name (e.g., North Boulevard Drain): ");
                    String location = scanner.nextLine().trim();
                    if (location.isEmpty()) {
                        System.out.println("[ERROR] Location name cannot be empty!");
                        break;
                    }

                    System.out.print("Enter Rainfall Amount in mm (e.g., 50.0): ");
                    double amount;
                    try {
                        amount = Double.parseDouble(scanner.nextLine().trim());
                        if (amount < 0) {
                            System.out.println("[ERROR] Rainfall amount cannot be negative!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format! Please enter a valid decimal number.");
                        break;
                    }

                    System.out.print("Enter Duration in hours (e.g., 2.0): ");
                    double duration;
                    try {
                        duration = Double.parseDouble(scanner.nextLine().trim());
                        if (duration <= 0) {
                            System.out.println("[ERROR] Duration must be greater than 0 hours!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format! Please enter a valid decimal number.");
                        break;
                    }

                    // Create new Rainfall object and add to manager
                    Rainfall record = new Rainfall(location, amount, duration);
                    rainfallManager.addRainfall(record);
                    break;

                case "2":
                    // Option 2: Display all rainfall records (Day 1)
                    rainfallManager.displayAllRecords();
                    break;

                case "3":
                    // Option 3: Filter heavy intensity zones (Day 1)
                    rainfallManager.displayHeavyRainfallAreas();
                    break;

                case "4":
                    // Option 4: Add new drainage system (Day 2)
                    System.out.println("\n--- [ENTER DRAINAGE SYSTEM DATA] ---");
                    
                    System.out.print("Enter Drainage ID (e.g., DRN-104): ");
                    String dId = scanner.nextLine().trim();
                    if (dId.isEmpty()) {
                        System.out.println("[ERROR] Drainage ID cannot be empty!");
                        break;
                    }

                    // Check for duplicate ID
                    if (drainageManager.findDrainageById(dId) != null) {
                        System.out.println("[ERROR] Drainage with ID '" + dId + "' already exists!");
                        break;
                    }

                    System.out.print("Enter Location Name (e.g., East Highway Underpass): ");
                    String dLoc = scanner.nextLine().trim();
                    if (dLoc.isEmpty()) {
                        System.out.println("[ERROR] Location name cannot be empty!");
                        break;
                    }

                    System.out.print("Enter Drainage Maximum Capacity in mm (e.g., 100.0): ");
                    double cap;
                    try {
                        cap = Double.parseDouble(scanner.nextLine().trim());
                        if (cap <= 0) {
                            System.out.println("[ERROR] Capacity must be greater than 0 mm!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format! Please enter a valid decimal number.");
                        break;
                    }

                    System.out.print("Enter Current Water Level in mm (e.g., 20.0): ");
                    double waterLevel;
                    try {
                        waterLevel = Double.parseDouble(scanner.nextLine().trim());
                        if (waterLevel < 0) {
                            System.out.println("[ERROR] Water level cannot be negative!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format! Please enter a valid decimal number.");
                        break;
                    }

                    // Create new Drainage object and add to manager
                    Drainage newDrain = new Drainage(dId, dLoc, cap, waterLevel);
                    drainageManager.addDrainage(newDrain);
                    break;

                case "5":
                    // Option 5: Display all drainage systems (Day 2)
                    drainageManager.displayAllDrainages();
                    break;

                case "6":
                    // Option 6: Calculate whether a drainage can handle rainfall (Day 2)
                    System.out.println("\n--- [CHECK DRAINAGE RAINFALL CAPACITY] ---");
                    System.out.print("Enter Drainage ID to inspect (e.g., DRN-101): ");
                    String targetId = scanner.nextLine().trim();

                    Drainage targetDrain = drainageManager.findDrainageById(targetId);
                    if (targetDrain == null) {
                        System.out.println("[ERROR] No drainage found with ID: " + targetId);
                        break;
                    }

                    // Display current baseline condition
                    System.out.println("\nFound Drainage:");
                    targetDrain.displayDetails();

                    // Check if rainfall data exists for this location
                    Rainfall existingRain = rainfallManager.findRainfallByLocation(targetDrain.getLocation());
                    double testRainAmount;

                    if (existingRain != null) {
                        System.out.printf("[INFO] Sensor rainfall of %.2f mm was observed at '%s'.\n", 
                                existingRain.getRainfallAmount(), targetDrain.getLocation());
                        System.out.print("Use this recorded rainfall (" + existingRain.getRainfallAmount() + " mm)? (Y/N): ");
                        String useExisting = scanner.nextLine().trim();

                        if (useExisting.equalsIgnoreCase("Y")) {
                            testRainAmount = existingRain.getRainfallAmount();
                        } else {
                            System.out.print("Enter custom rainfall amount in mm (e.g., 40.0): ");
                            try {
                                testRainAmount = Double.parseDouble(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("[ERROR] Invalid rainfall number format!");
                                break;
                            }
                        }
                    } else {
                        System.out.print("Enter expected rainfall amount in mm to test (e.g., 50.0): ");
                        try {
                            testRainAmount = Double.parseDouble(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("[ERROR] Invalid rainfall number format!");
                            break;
                        }
                    }

                    if (testRainAmount < 0) {
                        System.out.println("[ERROR] Rainfall amount cannot be negative!");
                        break;
                    }

                    // Perform and display coupled analysis
                    targetDrain.displayCoupledAnalysis(testRainAmount);
                    break;

                case "7":
                    // Option 7: City-wide coupling nowcast (Day 2)
                    drainageManager.coupleWithRainfallData(rainfallManager);
                    break;

                case "8":
                    // Option 8: Filter drainage systems at risk (Day 2)
                    drainageManager.displayOverflowRiskDrainages();
                    break;

                case "9":
                    // Option 9: Calculate flood risk for a specific drainage unit (Day 3)
                    System.out.println("\n--- [CALCULATE FLOOD RISK FOR DRAINAGE ZONE] ---");
                    System.out.print("Enter Drainage ID to evaluate (e.g., DRN-101): ");
                    String riskDrainId = scanner.nextLine().trim();

                    Drainage evalDrain = drainageManager.findDrainageById(riskDrainId);
                    if (evalDrain == null) {
                        System.out.println("[ERROR] No drainage found with ID: " + riskDrainId);
                        break;
                    }

                    // Check if rainfall data exists for this location
                    Rainfall matchedRain = rainfallManager.findRainfallByLocation(evalDrain.getLocation());
                    FloodRisk floodRisk;

                    if (matchedRain != null) {
                        System.out.println("[MATCH FOUND] Coupled with rainfall sensor data at: " + evalDrain.getLocation());
                        System.out.printf("Observed: %.2f mm over %.2f hrs (Intensity: %.2f mm/hr - %s)\n",
                                matchedRain.getRainfallAmount(), matchedRain.getDuration(),
                                matchedRain.getIntensity(), matchedRain.getIntensityLevel());
                        System.out.print("Use this observed rainfall data? (Y/N): ");
                        String useObserved = scanner.nextLine().trim();

                        if (useObserved.equalsIgnoreCase("Y")) {
                            // Directly couple Rainfall object and Drainage object
                            floodRisk = new FloodRisk(matchedRain, evalDrain);
                        } else {
                            System.out.print("Enter custom rainfall intensity in mm/hr (e.g., 35.0): ");
                            double customIntensity = Double.parseDouble(scanner.nextLine().trim());
                            System.out.print("Enter custom rainfall duration in hours (e.g., 2.0): ");
                            double customDuration = Double.parseDouble(scanner.nextLine().trim());
                            floodRisk = new FloodRisk(evalDrain.getLocation(), customIntensity, customDuration, 
                                                     evalDrain.getCapacity(), evalDrain.getCurrentWaterLevel());
                        }
                    } else {
                        System.out.println("[INFO] No sensor rainfall record found for: " + evalDrain.getLocation());
                        System.out.println("Please provide estimated storm parameters:");
                        System.out.print("Enter estimated rainfall intensity in mm/hr (e.g., 25.0): ");
                        double customIntensity = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter estimated rainfall duration in hours (e.g., 1.5): ");
                        double customDuration = Double.parseDouble(scanner.nextLine().trim());
                        floodRisk = new FloodRisk(evalDrain.getLocation(), customIntensity, customDuration, 
                                                 evalDrain.getCapacity(), evalDrain.getCurrentWaterLevel());
                    }

                    // Display full Flood Risk Report
                    floodRisk.displayRiskReport();
                    break;

                case "10":
                    // Option 10: Run city-wide flood risk assessment across all zones (Day 3)
                    drainageManager.evaluateFloodRisks(rainfallManager);
                    break;

                case "11":
                    // Option 11: Calculate custom flood risk using manual 4-variable input (Day 3)
                    System.out.println("\n--- [CUSTOM FLOOD RISK CALCULATOR (4 PARAMETERS)] ---");
                    System.out.print("Enter Location Name (e.g., Airport Express Underpass): ");
                    String simLocation = scanner.nextLine().trim();
                    if (simLocation.isEmpty()) {
                        System.out.println("[ERROR] Location name cannot be empty!");
                        break;
                    }

                    double simIntensity;
                    System.out.print("Enter Rainfall Intensity in mm/hr (e.g., 40.0): ");
                    try {
                        simIntensity = Double.parseDouble(scanner.nextLine().trim());
                        if (simIntensity < 0) {
                            System.out.println("[ERROR] Rainfall intensity cannot be negative!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for rainfall intensity!");
                        break;
                    }

                    double simDuration;
                    System.out.print("Enter Rainfall Duration in hours (e.g., 2.0): ");
                    try {
                        simDuration = Double.parseDouble(scanner.nextLine().trim());
                        if (simDuration <= 0) {
                            System.out.println("[ERROR] Duration must be greater than 0 hours!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for rainfall duration!");
                        break;
                    }

                    double simCapacity;
                    System.out.print("Enter Drainage Capacity in mm (e.g., 100.0): ");
                    try {
                        simCapacity = Double.parseDouble(scanner.nextLine().trim());
                        if (simCapacity <= 0) {
                            System.out.println("[ERROR] Drainage capacity must be greater than 0 mm!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for drainage capacity!");
                        break;
                    }

                    double simWaterLevel;
                    System.out.print("Enter Current Water Level in mm (e.g., 30.0): ");
                    try {
                        simWaterLevel = Double.parseDouble(scanner.nextLine().trim());
                        if (simWaterLevel < 0) {
                            System.out.println("[ERROR] Water level cannot be negative!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for water level!");
                        break;
                    }

                    // Create FloodRisk object combining all 4 parameters
                    FloodRisk customFloodRisk = new FloodRisk(simLocation, simIntensity, simDuration, 
                                                             simCapacity, simWaterLevel);
                    customFloodRisk.displayRiskReport();
                    break;

                case "13":
                    // Option 13: Generate evacuation advisory for a specific drainage zone (Day 4)
                    System.out.println("\n--- [GENERATE EVACUATION ADVISORY FOR DRAINAGE ZONE] ---");
                    System.out.print("Enter Drainage ID to evaluate (e.g., DRN-101): ");
                    String advDrainId = scanner.nextLine().trim();

                    Drainage advDrain = drainageManager.findDrainageById(advDrainId);
                    if (advDrain == null) {
                        System.out.println("[ERROR] No drainage found with ID: " + advDrainId);
                        break;
                    }

                    Rainfall advRain = rainfallManager.findRainfallByLocation(advDrain.getLocation());
                    FloodRisk advFloodRisk;

                    if (advRain != null) {
                        System.out.println("[MATCH] Coupled with sensor at: " + advDrain.getLocation());
                        System.out.printf("Observed: %.2f mm/hr, %.2f hrs (%s)%n",
                                advRain.getIntensity(), advRain.getDuration(), advRain.getIntensityLevel());
                        System.out.print("Use this observed rainfall data? (Y/N): ");
                        String useAdv = scanner.nextLine().trim();

                        if (useAdv.equalsIgnoreCase("Y")) {
                            advFloodRisk = new FloodRisk(advRain, advDrain);
                        } else {
                            System.out.print("Enter custom rainfall intensity in mm/hr: ");
                            double cI = Double.parseDouble(scanner.nextLine().trim());
                            System.out.print("Enter custom rainfall duration in hours: ");
                            double cD = Double.parseDouble(scanner.nextLine().trim());
                            advFloodRisk = new FloodRisk(advDrain.getLocation(), cI, cD,
                                    advDrain.getCapacity(), advDrain.getCurrentWaterLevel());
                        }
                    } else {
                        System.out.println("[INFO] No sensor rainfall data for: " + advDrain.getLocation());
                        System.out.print("Enter estimated rainfall intensity in mm/hr: ");
                        double cI2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter estimated rainfall duration in hours: ");
                        double cD2 = Double.parseDouble(scanner.nextLine().trim());
                        advFloodRisk = new FloodRisk(advDrain.getLocation(), cI2, cD2,
                                advDrain.getCapacity(), advDrain.getCurrentWaterLevel());
                    }

                    // Generate and display full Day 4 evacuation advisory
                    advFloodRisk.displayRiskReport();
                    EvacuationAdvisory advisory = new EvacuationAdvisory(advFloodRisk);
                    advisory.displayAdvisoryReport();
                    break;

                case "14":
                    // Option 14: City-wide evacuation advisory sweep across all zones (Day 4)
                    System.out.println("\n=============================================================");
                    System.out.println("        CITY-WIDE EVACUATION ADVISORY SWEEP (DAY 4)        ");
                    System.out.println("=============================================================");

                    if (drainageManager.getDrainageList().isEmpty()) {
                        System.out.println("[INFO] No drainage systems registered.");
                        break;
                    }

                    int critCount = 0, elevCount = 0, modCount = 0, normCount = 0;

                    for (Drainage sweepDrain : drainageManager.getDrainageList()) {
                        Rainfall sweepRain = rainfallManager.findRainfallByLocation(sweepDrain.getLocation());
                        FloodRisk sweepRisk;

                        if (sweepRain != null) {
                            sweepRisk = new FloodRisk(sweepRain, sweepDrain);
                        } else {
                            // No rainfall coupled: assume 0 intensity, baseline level only
                            sweepRisk = new FloodRisk(sweepDrain.getLocation(), 0.0, 1.0,
                                    sweepDrain.getCapacity(), sweepDrain.getCurrentWaterLevel());
                        }

                        EvacuationAdvisory sweepAdv = new EvacuationAdvisory(sweepRisk);
                        sweepAdv.displayAdvisoryReport();

                        switch (sweepAdv.getThreatLevel()) {
                            case "CRITICAL": critCount++; break;
                            case "ELEVATED": elevCount++; break;
                            case "MODERATE": modCount++; break;
                            default: normCount++; break;
                        }
                    }

                    System.out.println("\n-------------- CITY THREAT LEVEL SUMMARY ----------------");
                    System.out.println("  CRITICAL (MANDATORY EVACUATION) : " + critCount + " zone(s)");
                    System.out.println("  ELEVATED (RECOMMENDED EVACUATION): " + elevCount + " zone(s)");
                    System.out.println("  MODERATE (PRECAUTIONARY ADVISORY): " + modCount + " zone(s)");
                    System.out.println("  NORMAL   (NO ACTION REQUIRED)    : " + normCount + " zone(s)");
                    System.out.println("=============================================================");
                    break;

                case "15":
                    // Option 15: Generate custom evacuation advisory via manual 4-variable input (Day 4)
                    System.out.println("\n--- [CUSTOM EVACUATION ADVISORY CALCULATOR (4 PARAMETERS)] ---");
                    System.out.print("Enter Location Name (e.g., Airport Expressway Underpass): ");
                    String advSimLoc = scanner.nextLine().trim();
                    if (advSimLoc.isEmpty()) {
                        System.out.println("[ERROR] Location name cannot be empty!");
                        break;
                    }

                    double advSimIntensity;
                    System.out.print("Enter Rainfall Intensity in mm/hr (e.g., 40.0): ");
                    try {
                        advSimIntensity = Double.parseDouble(scanner.nextLine().trim());
                        if (advSimIntensity < 0) {
                            System.out.println("[ERROR] Rainfall intensity cannot be negative!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for rainfall intensity!");
                        break;
                    }

                    double advSimDuration;
                    System.out.print("Enter Rainfall Duration in hours (e.g., 2.0): ");
                    try {
                        advSimDuration = Double.parseDouble(scanner.nextLine().trim());
                        if (advSimDuration <= 0) {
                            System.out.println("[ERROR] Duration must be greater than 0 hours!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for rainfall duration!");
                        break;
                    }

                    double advSimCapacity;
                    System.out.print("Enter Drainage Capacity in mm (e.g., 100.0): ");
                    try {
                        advSimCapacity = Double.parseDouble(scanner.nextLine().trim());
                        if (advSimCapacity <= 0) {
                            System.out.println("[ERROR] Drainage capacity must be greater than 0 mm!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for drainage capacity!");
                        break;
                    }

                    double advSimWaterLevel;
                    System.out.print("Enter Current Water Level in mm (e.g., 30.0): ");
                    try {
                        advSimWaterLevel = Double.parseDouble(scanner.nextLine().trim());
                        if (advSimWaterLevel < 0) {
                            System.out.println("[ERROR] Water level cannot be negative!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid number format for water level!");
                        break;
                    }

                    FloodRisk customAdvRisk = new FloodRisk(advSimLoc, advSimIntensity, advSimDuration,
                            advSimCapacity, advSimWaterLevel);
                    customAdvRisk.displayRiskReport();
                    EvacuationAdvisory customAdvisory = new EvacuationAdvisory(customAdvRisk);
                    customAdvisory.displayAdvisoryReport();
                    break;

                case "16":
                    // Option 16: Exit program
                    System.out.println("\nThank you for using the Urban Flood Nowcasting System.");
                    System.out.println("Day 4 completed successfully! Exiting...");
                    running = false;
                    break;

                default:
                    System.out.println("\n[ERROR] Invalid option selected! Please choose a number between 1 and 16.");
                    break;
            }
        }

        scanner.close();
    }
}
