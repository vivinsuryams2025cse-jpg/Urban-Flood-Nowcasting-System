package com.flood.service;

import com.flood.entity.Rainfall;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * RainfallManager.java
 * 
 * Preserved Day 1 in-memory manager service.
 * Manages collections of Rainfall records, filters heavy rainfall areas, 
 * and provides lookup methods by location.
 */
@Service
public class RainfallManager {
    // List to store Rainfall objects in memory
    private ArrayList<Rainfall> rainfallRecords;

    /**
     * Default Constructor initializes empty ArrayList.
     */
    public RainfallManager() {
        rainfallRecords = new ArrayList<>();
    }

    /**
     * Adds a new Rainfall record to the list.
     * 
     * @param record The Rainfall object to add
     */
    public void addRainfall(Rainfall record) {
        rainfallRecords.add(record);
        System.out.println("\n[SUCCESS] Rainfall record saved successfully for: " + record.getLocation());
    }

    /**
     * Displays all stored rainfall records in a formatted list.
     */
    public void displayAllRecords() {
        if (rainfallRecords.isEmpty()) {
            System.out.println("\n[INFO] No rainfall records found. Please add a record first.");
            return;
        }

        System.out.println("\n==================================================");
        System.out.println("              ALL RAINFALL OBSERVATIONS           ");
        System.out.println("==================================================");

        for (int i = 0; i < rainfallRecords.size(); i++) {
            System.out.println("Record #" + (i + 1));
            rainfallRecords.get(i).displayDetails();
        }
        System.out.println("Total Records: " + rainfallRecords.size());
    }

    /**
     * Filters and displays only areas experiencing Heavy or Torrential rainfall.
     */
    public void displayHeavyRainfallAreas() {
        if (rainfallRecords.isEmpty()) {
            System.out.println("\n[INFO] No rainfall records available to evaluate.");
            return;
        }

        boolean found = false;
        System.out.println("\n==================================================");
        System.out.println("       HIGH-RISK HEAVY RAINFALL ALERT ZONES       ");
        System.out.println("==================================================");

        for (int i = 0; i < rainfallRecords.size(); i++) {
            Rainfall record = rainfallRecords.get(i);
            if (record.getIntensity() >= 7.6) {
                record.displayDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println("[INFO] No heavy or torrential rainfall detected in any area.");
        }
    }

    /**
     * Searches for a rainfall record by location name (case-insensitive).
     * 
     * @param location Name of location to look up
     * @return Rainfall object if found, null otherwise
     */
    public Rainfall findRainfallByLocation(String location) {
        for (Rainfall record : rainfallRecords) {
            if (record.getLocation().equalsIgnoreCase(location.trim())) {
                return record;
            }
        }
        return null;
    }

    /**
     * Returns the list of rainfall records.
     */
    public ArrayList<Rainfall> getRainfallRecords() {
        return rainfallRecords;
    }

    /**
     * Returns total count of records.
     */
    public int getRecordCount() {
        return rainfallRecords.size();
    }
}
