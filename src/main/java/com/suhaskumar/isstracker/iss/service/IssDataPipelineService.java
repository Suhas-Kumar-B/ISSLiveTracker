package com.suhaskumar.isstracker.iss.service;

import com.suhaskumar.isstracker.alerts.model.Alert;
import com.suhaskumar.isstracker.alerts.repository.AlertRepository;
import com.suhaskumar.isstracker.iss.dto.IssApiResponse;
import com.suhaskumar.isstracker.iss.model.IssPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class IssDataPipelineService {
    private static final Logger logger = LoggerFactory.getLogger(IssDataPipelineService.class);
    public static final String CURRENT_ISS_POSITION_KEY = "iss-position:current";
    private final AtomicBoolean isPaused = new AtomicBoolean(false);

    private final IssApiClient issApiClient;
    private final IssPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AlertRepository alertRepository; // To get all alerts

    public IssDataPipelineService(IssApiClient issApiClient, IssPersistenceService persistenceService, RedisTemplate<String, Object> redisTemplate, AlertRepository alertRepository) {
        this.issApiClient = issApiClient;
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
        this.alertRepository = alertRepository;
    }

    @Scheduled(fixedDelay = 5000) // Runs every 5 seconds
    public void collectAndStoreIssPosition() {
        if (isPaused.get()) {
            logger.trace("Data pipeline is paused. Skipping execution.");
            return;
        }
        try {
            IssApiResponse response = issApiClient.fetchIssPosition();
            if (response != null && "success".equals(response.getMessage())) {
                IssPosition newPosition = new IssPosition();
                newPosition.setLatitude(response.getIssPosition().getLatitude());
                newPosition.setLongitude(response.getIssPosition().getLongitude());
                newPosition.setTimestamp(response.getTimestamp());

                // Store in cache and database
                redisTemplate.opsForValue().set(CURRENT_ISS_POSITION_KEY, newPosition);
                persistenceService.savePosition(newPosition);
                logger.info("Successfully fetched and stored ISS position: {}", newPosition);

                // --- NEW: Check for real-time flyover alerts ---
                checkForFlyover(newPosition);
            }
        } catch (Exception e) {
            logger.error("Failed to execute data pipeline task: {}", e.getMessage());
        }
    }

    private void checkForFlyover(IssPosition currentPosition) {
        List<Alert> allAlerts = alertRepository.findAll();
        for (Alert alert : allAlerts) {
            double distance = calculateDistance(
                    currentPosition.getLatitude(), currentPosition.getLongitude(),
                    alert.getLatitude(), alert.getLongitude()
            );

            // Trigger alert if ISS is within ~100 km of the location
            if (distance <= 100) {
                logger.warn("!!! REAL-TIME FLYOVER ALERT !!!");
                logger.warn("The ISS is currently passing over '{}' (approx. {} km away)", alert.getLocationName(), String.format("%.2f", distance));
            }
        }
    }

    // Haversine formula to calculate distance between two lat/lon points in km
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


    public void pause() {
        isPaused.set(true);
        logger.warn("ISS data collection has been PAUSED.");
    }

    public void resume() {
        isPaused.set(false);
        logger.warn("ISS data collection has been RESUMED.");
    }
}