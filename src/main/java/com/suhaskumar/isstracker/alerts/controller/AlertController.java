package com.suhaskumar.isstracker.alerts.controller;

import com.suhaskumar.isstracker.alerts.model.Alert;
import com.suhaskumar.isstracker.alerts.service.AlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alerts", description = "Endpoints for location-based ISS pass alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert newAlert) {
        return ResponseEntity.ok(alertService.createAlert(newAlert));
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getUserAlerts() {
        return ResponseEntity.ok(alertService.getUserAlerts());
    }
}