package com.suhaskumar.isstracker.controller;

import com.suhaskumar.isstracker.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    // --- Add this method ---
    @GetMapping("/")
    public String home() {
        return "Welcome to the ISS Tracker API!";
    }

    // Endpoint to verify Redis connection directly
    @GetMapping("/api/redis-test")
    public String testRedis() {
        return testService.verifyRedisConnection();
    }

    // Endpoint to test Redis caching for ISS position
    @GetMapping("/api/iss-position")
    public Map<String, Double> getIssPosition() {
        return testService.getIssPosition();
    }

    // A secured endpoint outside the /api/** path
    @GetMapping("/admin/secure-data")
    public String getSecureData() {
        return "This is secure data, only accessible to authenticated users.";
    }
}