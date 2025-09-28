package com.suhaskumar.isstracker.iss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssApiResponse {
    private String message;
    private long timestamp;
    @JsonProperty("iss_position")
    private IssPositionDTO issPosition;
}