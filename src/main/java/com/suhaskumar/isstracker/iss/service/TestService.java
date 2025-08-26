package com.suhaskumar.isstracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Verifies the Redis connection by writing and reading a simple value.
     */
    public String verifyRedisConnection() {
        String key = "connection:test";
        String value = "Redis is working!";

        logger.info("Storing value in Redis: '{}' -> '{}'", key, value);
        redisTemplate.opsForValue().set(key, value, 60, TimeUnit.SECONDS);

        logger.info("Retrieving value from Redis for key: '{}'", key);
        String retrievedValue = (String) redisTemplate.opsForValue().get(key);
        logger.info("Retrieved value: '{}'", retrievedValue);

        return "Successfully stored and retrieved: '" + retrievedValue + "'";
    }

    /**
     * Fetches ISS position. This method is cached for 10 seconds.
     * The cache name is "iss-position".
     */
    @Cacheable(value = "iss-position", key = "'current'")
    public Map<String, Double> getIssPosition() {
        // This log will only appear when the method is actually executed (not cached)
        logger.info("--- FETCHING FRESH ISS POSITION (NOT FROM CACHE) ---");

        // In a real application, you would call an external API here.
        // We will simulate it by returning a map with the current time.
        return Map.of(
                "latitude", 20.5937,
                "longitude", 78.9629,
                "timestamp", (double) System.currentTimeMillis()
        );
    }
}