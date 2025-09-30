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

/**
 * Service responsible for collecting ISS position data,
 * storing it in cache & database, and checking for location-based alerts.
 */
@Service
public class IssDataPipelineService {

    // --- LOGGER ---
    private static final Logger logger = LoggerFactory.getLogger(IssDataPipelineService.class);

    // --- REDIS KEY FOR CURRENT POSITION ---
    public static final String CURRENT_ISS_POSITION_KEY = "iss-position:current";

    // --- ALERT TRIGGER DISTANCE ---
    private static final double ALERT_DISTANCE_THRESHOLD_KM = 100.0;

    // --- CONTROL FLAG TO PAUSE/RESUME PIPELINE ---
    private final AtomicBoolean isPaused = new AtomicBoolean(false);

    // --- DEPENDENCIES ---
    private final IssApiClient issApiClient;
    private final IssPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AlertRepository alertRepository;

    /**
     * Constructor injection for dependencies.
     */
    public IssDataPipelineService(IssApiClient issApiClient,
                                  IssPersistenceService persistenceService,
                                  RedisTemplate<String, Object> redisTemplate,
                                  AlertRepository alertRepository) {
        this.issApiClient = issApiClient;
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
        this.alertRepository = alertRepository;
    }

    /**
     * Scheduled task that runs every 5 seconds.
     * Fetches the current ISS position from API,
     * saves it to Redis + DB, and checks for alert conditions.
     */
    @Scheduled(fixedDelay = 5000) // Runs every 5 seconds
    public void collectAndStoreIssPosition() {
        // Skip if paused
        if (isPaused.get()) {
            logger.trace("Data pipeline is paused. Skipping execution.");
            return;
        }

        try {
            // Fetch ISS position from external API
            IssApiResponse response = issApiClient.fetchIssPosition();

            // Proceed only if API returns success
            if (response != null && "success".equals(response.getMessage())) {
                IssPosition newPosition = new IssPosition();
                newPosition.setLatitude(response.getIssPosition().getLatitude());
                newPosition.setLongitude(response.getIssPosition().getLongitude());
                newPosition.setTimestamp(response.getTimestamp());

                // Store in Redis (cache)
                redisTemplate.opsForValue().set(CURRENT_ISS_POSITION_KEY, newPosition);

                // Persist into database
                persistenceService.savePosition(newPosition);

                logger.info("Successfully fetched and stored ISS position: {}", newPosition);

                // Check if this triggers any real-time alerts
                checkForFlyover(newPosition);
            }
        } catch (Exception e) {
            // Log full exception with stack trace
            logger.error("Failed to execute data pipeline task", e);
        }
    }

    /**
     * Check all registered alerts to see if ISS is within threshold distance.
     * If within ~100 km, log a warning.
     */
    private void checkForFlyover(IssPosition currentPosition) {
        List<Alert> allAlerts = alertRepository.findAll();

        for (Alert alert : allAlerts) {
            double distance = calculateDistance(
                    currentPosition.getLatitude(), currentPosition.getLongitude(),
                    alert.getLatitude(), alert.getLongitude()
            );

            // Trigger alert if ISS is within defined threshold
            if (distance <= ALERT_DISTANCE_THRESHOLD_KM && logger.isWarnEnabled()) {
                logger.warn("!!! REAL-TIME FLYOVER ALERT !!!");
                logger.warn("The ISS is currently passing over '{}' (approx. {} km away)",
                        alert.getLocationName(),
                        String.format("%.2f", distance));
            }

        }
    }

    /**
     * Utility: Haversine formula to calculate great-circle distance (km)
     * between two lat/lon points on Earth.
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c; // Final distance in km
    }

    /**
     * Pause the data collection pipeline.
     */
    public void pause() {
        isPaused.set(true);
        logger.info("ISS data collection has been PAUSED.");
    }

    /**
     * Resume the data collection pipeline.
     */
    public void resume() {
        isPaused.set(false);
        logger.info("ISS data collection has been RESUMED.");
    }
}
