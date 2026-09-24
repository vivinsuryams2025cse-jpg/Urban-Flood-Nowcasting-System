package com.flood;

import java.util.Scanner;

/**
 * Main.java
 * 
 * Main entry point for the Urban Flood Nowcasting System (Day 2).
 * Integrates the Drainage System Module with Day 1 Rainfall Observations.
 * Presents an interactive text-based console menu for users to:
 * - Enter and view rainfall observation data (Day 1)
 * - Enter and view urban drainage systems (Day 2)
 * - Calculate whether drainage systems can handle rainfall (Day 2)
 * - Run city-wide rainfall-drainage coupling nowcast (Day 2)
 * - Filter and display drainage overflow warnings (Day 2)
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RainfallManager rainfallManager = new RainfallManager();
        DrainageManager drainageManager = new DrainageManager();

        // -------------------------------------------------------------
        // INITIAL SAMPLE DATA (Day 1 Rainfall & Day 2 Drainage)
        // Allows immediate testing of viewing & coupling nowcast features!
        // -------------------------------------------------------------
        
        // Day 1 Sample Rainfall Records
        rainfallManager.addRainfall(new Rainfall("Sector 4 Central Canal", 45.0, 1.5));
        rainfallManager.addRainfall(new Rainfall("Downtown Metro Subway Drain", 85.0, 1.0));

        // Day 2 Sample Drainage Records (Coupled with the above zones)
        drainageManager.addDrainage(new Drainage("DRN-101", "Sector 4 Central Canal", 100.0, 30.0));
        drainageManager.addDrainage(new Drainage("DRN-102", "Downtown Metro Subway Drain", 80.0, 25.0));
        drainageManager.addDrainage(new Drainage("DRN-103", "Riverside Boulevard Culvert", 120.0, 15.0));

        boolean running = true;

        System.out.println("=============================================================");
        System.out.println("  URBAN FLOOD NOWCASTING SYSTEM (Drainage & Rainfall Coupling)");
        System.out.println("  [DAY 2: Drainage System & Rainfall-Drainage Coupling]");
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
            System.out.println("  [System]");
            System.out.println("  9. Exit System");
            System.out.print("Please enter your choice (1-9): ");

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
                    // Option 9: Exit program
                    System.out.println("\nThank you for using the Urban Flood Nowcasting System.");
                    System.out.println("Day 2 completed successfully! Exiting...");
                    running = false;
                    break;

                default:
                    System.out.println("\n[ERROR] Invalid option selected! Please choose a number between 1 and 9.");
                    break;
            }
        }

        scanner.close();
    }
}
