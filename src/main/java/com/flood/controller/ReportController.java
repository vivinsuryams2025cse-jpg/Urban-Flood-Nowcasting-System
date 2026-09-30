package com.flood.controller;

import com.flood.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ReportController.java
 * 
 * REST controller for city-wide flood reports and summary statistics.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET /api/reports/city-wide
     * Generates a comprehensive city-wide nowcasting report across all monitored drainage zones.
     */
    @GetMapping("/city-wide")
    public ResponseEntity<Map<String, Object>> getCityWideReport() {
        return ResponseEntity.ok(reportService.generateCityWideReport());
    }

    /**
     * GET /api/reports/summary
     * Generates city-wide flood threat counts and summary metrics.
     */
    @GetMapping("/summary")
    public ResponseEntity<Object> getSummaryReport() {
        Map<String, Object> report = reportService.generateCityWideReport();
        return ResponseEntity.ok(report.get("summary"));
    }
}
