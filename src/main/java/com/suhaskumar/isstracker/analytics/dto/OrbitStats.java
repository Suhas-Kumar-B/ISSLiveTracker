package com.suhaskumar.isstracker.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrbitStats {
    private long totalPositionsTracked;
    private double averageLatitude;
    private double averageLongitude;
}
