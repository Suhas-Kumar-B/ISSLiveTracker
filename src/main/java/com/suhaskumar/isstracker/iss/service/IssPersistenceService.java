package com.suhaskumar.isstracker.iss.service;

import com.suhaskumar.isstracker.iss.model.IssPosition;
import com.suhaskumar.isstracker.iss.repository.IssPositionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class IssPersistenceService {
    private final IssPositionRepository issPositionRepository;

    // Using constructor injection
    public IssPersistenceService(IssPositionRepository issPositionRepository) {
        this.issPositionRepository = issPositionRepository;
    }

    @Async
    public void savePosition(IssPosition position) {
        issPositionRepository.save(position);
    }
}