package com.suhaskumar.isstracker.analytics.service;

import com.suhaskumar.isstracker.analytics.dto.OrbitStats;
import com.suhaskumar.isstracker.iss.repository.IssPositionRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final IssPositionRepository issPositionRepository;

    // Using constructor injection
    public AnalyticsService(IssPositionRepository issPositionRepository) {
        this.issPositionRepository = issPositionRepository;
    }

    public OrbitStats getOrbitStatistics() {
        long count = issPositionRepository.count();
        return new OrbitStats(count, 0.0, 0.0); // Placeholder averages
    }
}