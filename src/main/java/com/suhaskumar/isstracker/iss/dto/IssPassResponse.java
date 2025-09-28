package com.suhaskumar.isstracker.iss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class IssPassResponse {
    private String message;
    @JsonProperty("request")
    private PassRequest request;
    @JsonProperty("response")
    private List<Pass> passes;

    @Data
    public static class PassRequest {
        private double latitude;
        private double longitude;
        private int altitude;
        private int passes;
        private long datetime;
    }

    @Data
    public static class Pass {
        private int duration;
        @JsonProperty("risetime")
        private long riseTime;
    }
}
