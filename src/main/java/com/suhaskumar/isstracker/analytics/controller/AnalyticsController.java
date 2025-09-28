package com.suhaskumar.isstracker.analytics.controller;

import com.suhaskumar.isstracker.analytics.dto.OrbitStats;
import com.suhaskumar.isstracker.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Endpoints for ISS orbital statistics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // Using constructor injection
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Operation(summary = "Get basic statistics about the stored ISS orbital data")
    @GetMapping("/orbit-stats")
    public ResponseEntity<OrbitStats> getOrbitStatistics() {
        return ResponseEntity.ok(analyticsService.getOrbitStatistics());
    }
}