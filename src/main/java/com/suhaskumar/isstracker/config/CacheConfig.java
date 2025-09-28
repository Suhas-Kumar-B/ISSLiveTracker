package com.suhaskumar.isstracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.retry.annotation.EnableRetry;

import java.time.Duration;

@Configuration
@EnableRetry
public class CacheConfig {

    /**
     * Configures the CacheManager to use our custom, time-aware ObjectMapper.
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        // Create a JSON serializer that uses our configured ObjectMapper
        RedisSerializationContext.SerializationPair<Object> jsonSerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        // Configure the "iss-position" cache
        RedisCacheConfiguration issCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(10))
                .serializeValuesWith(jsonSerializer); // Use the configured serializer

        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration("iss-position", issCacheConfig)
                .build();
    }
}
