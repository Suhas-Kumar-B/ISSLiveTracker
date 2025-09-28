package com.suhaskumar.isstracker.iss.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "iss_positions")
@Data
@EntityListeners(AuditingEntityListener.class) // Enable JPA Auditing for this entity
public class IssPosition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    private long timestamp;

    @CreatedDate // Automatically set by Spring Data JPA on creation
    private Instant recordedAt;

    @Version // For optimistic locking to prevent concurrent update issues
    private Long version;
}