package com.suhaskumar.isstracker.alerts.model;

import lombok.Data;
// import jakarta.persistence.*; // Uncomment when creating the entity

// @Entity
// @Table(name = "alerts")
@Data
public class Alert {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String locationName;
    private double latitude;
    private double longitude;
    private String notificationType; // e.g., "EMAIL", "SMS"
}



