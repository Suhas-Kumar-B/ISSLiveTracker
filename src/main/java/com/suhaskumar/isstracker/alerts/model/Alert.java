package com.suhaskumar.isstracker.alerts.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "alerts")
@Data
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String locationName;
    private double latitude;
    private double longitude;
    private String notificationType; // e.g., "EMAIL", "SMS"
}