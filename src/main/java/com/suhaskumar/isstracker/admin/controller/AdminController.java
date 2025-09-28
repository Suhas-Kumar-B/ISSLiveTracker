package com.suhaskumar.isstracker.admin.controller;

import com.suhaskumar.isstracker.iss.service.IssDataPipelineService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints for administrative tasks")
@SecurityRequirement(name = "basicAuth")
@Hidden
public class AdminController {

    private final HealthEndpoint healthEndpoint;
    private final RedisConnectionFactory redisConnectionFactory;
    private final IssDataPipelineService pipelineService;

    // Using constructor injection
    public AdminController(HealthEndpoint healthEndpoint, RedisConnectionFactory redisConnectionFactory, IssDataPipelineService pipelineService) {
        this.healthEndpoint = healthEndpoint;
        this.redisConnectionFactory = redisConnectionFactory;
        this.pipelineService = pipelineService;
    }

    @Operation(summary = "Get detailed system health status")
    @GetMapping("/health")
    public ResponseEntity<HealthComponent> getSystemHealth() {
        return ResponseEntity.ok(healthEndpoint.health());
    }

    @Operation(summary = "Clear the entire Redis cache")
    @PostMapping("/cache/clear")
    public ResponseEntity<Map<String, String>> clearRedisCache() {
        redisConnectionFactory.getConnection().serverCommands().flushAll();
        return ResponseEntity.ok(Map.of("message", "Redis cache cleared successfully."));
    }

    @Operation(summary = "Pause the scheduled data collection pipeline")
    @PostMapping("/pipeline/pause")
    public ResponseEntity<Map<String, String>> pausePipeline() {
        pipelineService.pause();
        return ResponseEntity.ok(Map.of("message", "Data pipeline paused."));
    }

    @Operation(summary = "Resume the scheduled data collection pipeline")
    @PostMapping("/pipeline/resume")
    public ResponseEntity<Map<String, String>> resumePipeline() {
        pipelineService.resume();
        return ResponseEntity.ok(Map.of("message", "Data pipeline resumed."));
    }
}