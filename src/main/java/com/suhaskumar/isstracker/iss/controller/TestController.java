package com.suhaskumar.isstracker.iss.controller;

import com.suhaskumar.isstracker.iss.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * A controller with various endpoints for development and verification purposes.
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/")
    public String home() {
        return "Welcome to the ISS Tracker API!";
    }

    @GetMapping("/api/redis-test")
    public String testRedis() {
        return testService.verifyRedisConnection();
    }

    @GetMapping("/api/cache-test")
    public Map<String, Double> getCacheTest() {
        return testService.getCacheTest();
    }

    @GetMapping("/admin/secure-data")
    public String getSecureData() {
        return "This is secure data, only accessible to authenticated users.";
    }
}
