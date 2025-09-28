package com.suhaskumar.isstracker.alerts.controller;

import com.suhaskumar.isstracker.alerts.model.Alert;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alerts", description = "Endpoints for location-based ISS pass alerts")
public class AlertController {

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert newAlert) {
        // Placeholder for alert creation logic
        return ResponseEntity.ok(newAlert);
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getUserAlerts() {
        // Placeholder for fetching user alerts
        return ResponseEntity.ok(Collections.emptyList());
    }
}