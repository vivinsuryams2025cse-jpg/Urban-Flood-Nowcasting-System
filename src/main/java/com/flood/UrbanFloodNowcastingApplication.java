package com.flood;

import com.flood.entity.Drainage;
import com.flood.entity.Rainfall;
import com.flood.repository.DrainageRepository;
import com.flood.repository.RainfallRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * UrbanFloodNowcastingApplication.java
 * 
 * Main Spring Boot Application Entry Point for the Urban Flood Nowcasting System.
 * Layered Architecture:
 * - controller/ : REST Controllers handling HTTP requests
 * - dto/        : Request and Response Data Transfer Objects with Bean Validation
 * - entity/     : JPA Entities mapped to relational database tables
 * - repository/ : Spring Data JPA Repositories
 * - service/    : Business logic, hydrological calculations, nowcasting algorithms
 * - exception/  : Centralized Global Exception Handling
 * - config/     : Database and JPA configuration
 */
@SpringBootApplication
public class UrbanFloodNowcastingApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanFloodNowcastingApplication.class, args);
    }

    /**
     * Pre-populates sample rainfall and drainage data upon startup
     * if the database tables are empty.
     */
    @Bean
    public CommandLineRunner initSampleData(RainfallRepository rainfallRepository,
                                            DrainageRepository drainageRepository) {
        return args -> {
            if (rainfallRepository.count() == 0) {
                rainfallRepository.save(new Rainfall("Sector 4 Central Canal", 45.0, 1.5));
                rainfallRepository.save(new Rainfall("Downtown Metro Subway Drain", 85.0, 1.0));
                rainfallRepository.save(new Rainfall("Riverside Boulevard Culvert", 10.0, 2.0));
                System.out.println("[BOOTSTRAP] Sample rainfall observations initialized.");
            }

            if (drainageRepository.count() == 0) {
                drainageRepository.save(new Drainage("DRN-101", "Sector 4 Central Canal", 100.0, 30.0));
                drainageRepository.save(new Drainage("DRN-102", "Downtown Metro Subway Drain", 80.0, 25.0));
                drainageRepository.save(new Drainage("DRN-103", "Riverside Boulevard Culvert", 120.0, 15.0));
                System.out.println("[BOOTSTRAP] Sample drainage channel records initialized.");
            }

            System.out.println("\n=============================================================");
            System.out.println("  URBAN FLOOD NOWCASTING SYSTEM - SPRING BOOT SERVER READY   ");
            System.out.println("  Port: 8080 | Layered Spring Boot Architecture Initialized  ");
            System.out.println("=============================================================\n");
        };
    }
}
