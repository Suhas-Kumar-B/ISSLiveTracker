package com.suhaskumar.isstracker.iss.service;

import com.suhaskumar.isstracker.iss.dto.IssApiResponse;
import com.suhaskumar.isstracker.iss.model.IssPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class IssDataPipelineService {
    private static final Logger logger = LoggerFactory.getLogger(IssDataPipelineService.class);
    public static final String CURRENT_ISS_POSITION_KEY = "iss-position:current";
    private final AtomicBoolean isPaused = new AtomicBoolean(false);

    private final IssApiClient issApiClient;
    private final IssPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;

    // Using constructor injection
    public IssDataPipelineService(IssApiClient issApiClient, IssPersistenceService persistenceService, RedisTemplate<String, Object> redisTemplate) {
        this.issApiClient = issApiClient;
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedDelay = 5000)
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

                redisTemplate.opsForValue().set(CURRENT_ISS_POSITION_KEY, newPosition);
                persistenceService.savePosition(newPosition);

                logger.info("Successfully fetched and queued ISS position for storage: {}", newPosition);
            }
        } catch (Exception e) {
            logger.error("Failed to execute data pipeline task: {}", e.getMessage());
        }
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