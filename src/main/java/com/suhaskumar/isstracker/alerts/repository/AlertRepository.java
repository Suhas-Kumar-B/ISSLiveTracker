package com.suhaskumar.isstracker.alerts.repository;

import com.suhaskumar.isstracker.alerts.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByLatitudeAndLongitude(double latitude, double longitude);
}