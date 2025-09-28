package com.suhaskumar.isstracker.iss.service;

import com.suhaskumar.isstracker.common.exception.ApiRequestException;
import com.suhaskumar.isstracker.iss.dto.IssApiResponse;
import com.suhaskumar.isstracker.iss.dto.IssPassResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class IssApiClient {
    private static final Logger logger = LoggerFactory.getLogger(IssApiClient.class);
    private final RestTemplate restTemplate;
    private final String issApiBaseUrl;

    // Using constructor injection
    public IssApiClient(@Value("${api.iss.base-url}") String issApiBaseUrl) {
        this.restTemplate = new RestTemplate();
        this.issApiBaseUrl = issApiBaseUrl;
    }

    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public IssApiResponse fetchIssPosition() {
        String url = issApiBaseUrl + "/iss-now.json";
        logger.info("Attempting to fetch ISS position from API: {}", url);
        return restTemplate.getForObject(url, IssApiResponse.class);
    }

    @Recover
    public IssApiResponse recover(RestClientException e) {
        logger.error("All retry attempts failed for fetching ISS position. Error: {}", e.getMessage());
        throw new ApiRequestException("Could not fetch ISS position from external API after multiple retries.", e);
    }

    public IssPassResponse fetchIssPasses(double lat, double lon) {
        String url = UriComponentsBuilder.fromHttpUrl(issApiBaseUrl + "/iss-pass.json")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .toUriString();
        logger.info("Attempting to fetch ISS passes for lat={}, lon={}", lat, lon);
        return restTemplate.getForObject(url, IssPassResponse.class);
    }
}