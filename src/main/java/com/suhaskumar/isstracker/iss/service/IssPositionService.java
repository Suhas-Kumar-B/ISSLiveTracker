package com.suhaskumar.isstracker.iss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhaskumar.isstracker.iss.dto.IssPassResponse;
import com.suhaskumar.isstracker.iss.model.IssPosition;
import com.suhaskumar.isstracker.iss.repository.IssPositionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class IssPositionService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final IssPositionRepository issPositionRepository;
    private final ObjectMapper objectMapper;
    private final IssApiClient issApiClient;

    public IssPositionService(RedisTemplate<String, Object> redisTemplate, IssPositionRepository issPositionRepository, ObjectMapper objectMapper, IssApiClient issApiClient) {
        this.redisTemplate = redisTemplate;
        this.issPositionRepository = issPositionRepository;
        this.objectMapper = objectMapper;
        this.issApiClient = issApiClient;
    }

    public IssPosition getCurrentPosition() {
        Object rawData = redisTemplate.opsForValue().get(IssDataPipelineService.CURRENT_ISS_POSITION_KEY);
        if (rawData == null) return null;
        return objectMapper.convertValue(rawData, IssPosition.class);
    }

    public Page<IssPosition> getHistory(Pageable pageable, Instant startTime, Instant endTime) {
        // Build a dynamic query using Specifications
        Specification<IssPosition> spec = Specification.where(null);
        if (startTime != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("recordedAt"), startTime));
        }
        if (endTime != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("recordedAt"), endTime));
        }
        return issPositionRepository.findAll(spec, pageable);
    }

    public IssPassResponse getUpcomingPasses(double lat, double lon) {
        return issApiClient.fetchIssPasses(lat, lon);
    }
}