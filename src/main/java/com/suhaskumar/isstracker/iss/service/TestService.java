package com.suhaskumar.isstracker.iss.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A service with utility methods for verifying connections and features during development.
 * Can be removed in a final production build.
 */
@Service
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public TestService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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
     * A simple cacheable method to test if the CacheConfig is working correctly.
     */
    @Cacheable(value = "iss-position", key = "'test-cache'")
    public Map<String, Double> getCacheTest() {
        logger.info("--- EXECUTING CACHEABLE TEST METHOD (NOT FROM CACHE) ---");
        return Map.of(
                "latitude", 12.2958,
                "longitude", 76.6394,
                "timestamp", (double) System.currentTimeMillis()
        );
    }
}
