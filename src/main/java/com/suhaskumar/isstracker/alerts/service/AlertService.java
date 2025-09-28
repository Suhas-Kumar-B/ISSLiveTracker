package com.suhaskumar.isstracker.alerts.service;

import com.suhaskumar.isstracker.alerts.model.Alert;
import com.suhaskumar.isstracker.alerts.repository.AlertRepository;
import com.suhaskumar.isstracker.iss.dto.IssPassResponse;
import com.suhaskumar.isstracker.iss.service.IssPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    private final AlertRepository alertRepository;
    private final IssPositionService issPositionService;

    // A simple in-memory set to track which pass notifications have been sent
    private final Set<String> notifiedPasses = Collections.synchronizedSet(new HashSet<>());

    public AlertService(AlertRepository alertRepository, IssPositionService issPositionService) {
        this.alertRepository = alertRepository;
        this.issPositionService = issPositionService;
    }

    public Alert createAlert(Alert newAlert) {
        logger.info("New alert created: {}", newAlert);
        return alertRepository.save(newAlert);
    }

    public List<Alert> getUserAlerts() {
        return alertRepository.findAll();
    }

    /**
     * A scheduled task that runs every 15 minutes to check for upcoming passes.
     */
    @Scheduled(fixedRate = 900000) // Runs every 15 minutes (900,000 milliseconds)
    public void checkForUpcomingPasses() {
        logger.info("--- Running scheduled job: Checking for 30-minute advance pass alerts ---");
        List<Alert> allAlerts = alertRepository.findAll();

        if (allAlerts.isEmpty()) {
            logger.info("No alerts configured. Skipping pass check.");
            return;
        }

        for (Alert alert : allAlerts) {
            try {
                IssPassResponse passResponse = issPositionService.getUpcomingPasses(alert.getLatitude(), alert.getLongitude());

                if (passResponse != null && passResponse.getPasses() != null) {
                    for (IssPassResponse.Pass pass : passResponse.getPasses()) {
                        Instant passTime = Instant.ofEpochSecond(pass.getRiseTime());
                        String passIdentifier = alert.getId() + ":" + pass.getRiseTime();

                        // Check if the pass is within the next 30 minutes and we haven't notified for it yet
                        if (passTime.isBefore(Instant.now().plus(30, ChronoUnit.MINUTES)) && !notifiedPasses.contains(passIdentifier)) {

                            // --- TRIGGER 30-MINUTE ADVANCE NOTIFICATION ---
                            logger.warn("!!! 30-MINUTE ADVANCE ALERT !!!");
                            logger.warn("ISS pass is approaching for alert: '{}'", alert.getLocationName());
                            logger.warn("Approximate Pass Time: {}", passTime);

                            // Mark this pass as notified to prevent duplicate alerts
                            notifiedPasses.add(passIdentifier);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to check passes for alert ID {}: {}", alert.getId(), e.getMessage());
            }
        }
        logger.info("--- Finished scheduled job: 30-minute pass check complete ---");
    }
}