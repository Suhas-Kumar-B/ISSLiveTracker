package com.suhaskumar.isstracker.analytics.service;

import com.suhaskumar.isstracker.analytics.dto.OrbitStats;
import com.suhaskumar.isstracker.iss.repository.IssPositionRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final IssPositionRepository issPositionRepository;

    public AnalyticsService(IssPositionRepository issPositionRepository) {
        this.issPositionRepository = issPositionRepository;
    }

    public OrbitStats getOrbitStatistics() {
        long count = issPositionRepository.count();
        double avgLat = issPositionRepository.getAverageLatitude();
        double avgLon = issPositionRepository.getAverageLongitude();
        return new OrbitStats(count, avgLat, avgLon);
    }
}