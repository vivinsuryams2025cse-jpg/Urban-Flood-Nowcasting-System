package com.flood;

import java.util.ArrayList;

/**
 * RainfallManager.java
 * 
 * Manages the collection of rainfall observation records.
 * Uses an ArrayList to store Rainfall objects and provides methods to:
 * - Add a new rainfall record
 * - Display all saved records
 * - Filter and display heavy/high-intensity rainfall zones
 */
public class RainfallManager {
    // List to hold multiple Rainfall objects in memory
    private ArrayList<Rainfall> rainfallList;

    /**
     * Constructor initializes an empty ArrayList.
     */
    public RainfallManager() {
        rainfallList = new ArrayList<>();
    }

    /**
     * Adds a new Rainfall record to the list.
     * 
     * @param rainfall The Rainfall object to add
     */
    public void addRainfall(Rainfall rainfall) {
        rainfallList.add(rainfall);
        System.out.println("\n[SUCCESS] Rainfall record saved successfully for: " + rainfall.getLocation());
    }

    /**
     * Displays all rainfall records in a numbered list.
     */
    public void displayAllRecords() {
        if (rainfallList.isEmpty()) {
            System.out.println("\n[INFO] No rainfall records found. Please add records first.");
            return;
        }

        System.out.println("\n==================================================");
        System.out.println("             ALL RAINFALL OBSERVATIONS            ");
        System.out.println("==================================================");

        for (int i = 0; i < rainfallList.size(); i++) {
            System.out.println("Record #" + (i + 1));
            rainfallList.get(i).displayDetails();
        }
        System.out.println("Total Records: " + rainfallList.size());
    }

    /**
     * Filters and displays areas experiencing Heavy (>= 7.6 mm/hr)
     * or Torrential rainfall that could threaten urban drainage.
     */
    public void displayHeavyRainfallAreas() {
        if (rainfallList.isEmpty()) {
            System.out.println("\n[INFO] No rainfall records available to evaluate.");
            return;
        }

        boolean found = false;
        System.out.println("\n==================================================");
        System.out.println("      HIGH-INTENSITY / HEAVY RAINFALL ZONES       ");
        System.out.println("==================================================");

        for (int i = 0; i < rainfallList.size(); i++) {
            Rainfall record = rainfallList.get(i);
            // Check if intensity is 7.6 mm/hr or higher (Heavy/Torrential)
            if (record.getIntensity() >= 7.6) {
                record.displayDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println("[INFO] No heavy or torrential rainfall detected. All monitored areas are safe.");
        }
    }

    /**
     * Returns the total count of saved records.
     */
    public int getRecordCount() {
        return rainfallList.size();
    }

    /**
     * Finds a Rainfall record by location name (case-insensitive).
     * Used to connect Day 1 rainfall data with Day 2 drainage systems.
     * 
     * @param location Location name to find
     * @return Rainfall object if found, null otherwise
     */
    public Rainfall findRainfallByLocation(String location) {
        for (Rainfall record : rainfallList) {
            if (record.getLocation().equalsIgnoreCase(location.trim())) {
                return record;
            }
        }
        return null;
    }

    /**
     * Returns the list of all rainfall records.
     */
    public ArrayList<Rainfall> getRainfallList() {
        return rainfallList;
    }
}
