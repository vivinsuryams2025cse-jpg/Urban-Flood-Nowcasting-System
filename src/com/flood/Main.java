package com.flood;

import java.util.Scanner;

/**
 * Main.java
 * 
 * Main entry point for the Urban Flood Nowcasting System (Day 1).
 * Presents an interactive text-based console menu for users to:
 * - Enter new rainfall data
 * - View existing rainfall records
 * - View high-intensity rainfall alerts
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RainfallManager manager = new RainfallManager();

        // Add 2 initial sample records so you can test viewing right away!
        manager.addRainfall(new Rainfall("Sector 4 Central Canal", 45.0, 1.5));
        manager.addRainfall(new Rainfall("Downtown Metro Subway Drain", 85.0, 1.0));

        boolean running = true;

        System.out.println("=============================================================");
        System.out.println("  URBAN FLOOD NOWCASTING SYSTEM (Drainage & Rainfall Coupling)");
        System.out.println("  [DAY 1: Project Setup & Rainfall Observation Module]");
        System.out.println("=============================================================");

        while (running) {
            // Display Console Menu
            System.out.println("\n----------------- MAIN MENU -----------------");
            System.out.println("1. Add New Rainfall Record");
            System.out.println("2. Display All Rainfall Records");
            System.out.println("3. Display High-Risk / Heavy Rainfall Zones");
            System.out.println("4. Exit System");
            System.out.print("Please enter your choice (1-4): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Option 1: Add new record
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
                    manager.addRainfall(record);
                    break;

                case "2":
                    // Option 2: Display all records
                    manager.displayAllRecords();
                    break;

                case "3":
                    // Option 3: Filter heavy intensity zones
                    manager.displayHeavyRainfallAreas();
                    break;

                case "4":
                    // Option 4: Exit program
                    System.out.println("\nThank you for using the Urban Flood Nowcasting System.");
                    System.out.println("Day 1 completed successfully! Exiting...");
                    running = false;
                    break;

                default:
                    System.out.println("\n[ERROR] Invalid option selected! Please choose 1, 2, 3, or 4.");
                    break;
            }
        }

        scanner.close();
    }
}
