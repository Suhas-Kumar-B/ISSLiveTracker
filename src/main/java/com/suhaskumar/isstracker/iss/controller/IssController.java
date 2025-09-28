package com.suhaskumar.isstracker.iss.controller;

import com.suhaskumar.isstracker.iss.dto.IssPassResponse;
import com.suhaskumar.isstracker.iss.model.IssPosition;
import com.suhaskumar.isstracker.iss.service.IssPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/iss")
@Tag(name = "ISS Position", description = "Endpoints for ISS position data")
@Validated
public class IssController {

    private final IssPositionService issPositionService;

    public IssController(IssPositionService issPositionService) {
        this.issPositionService = issPositionService;
    }

    @Operation(summary = "Get the current live position of the ISS")
    @GetMapping("/current")
    public ResponseEntity<IssPosition> getCurrentPosition() {
        IssPosition currentPosition = issPositionService.getCurrentPosition();
        return currentPosition != null ? ResponseEntity.ok(currentPosition) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get a paginated history of ISS positions, with optional date filtering")
    @GetMapping("/history")
    public Page<IssPosition> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("recordedAt").descending());
        return issPositionService.getHistory(pageable, startTime, endTime);
    }

    @Operation(summary = "Get upcoming ISS pass predictions for a given location")
    @GetMapping("/passes/{lat}/{lon}")
    public ResponseEntity<IssPassResponse> getPasses(
            @PathVariable @DecimalMin("-90.0") @DecimalMax("90.0") double lat,
            @PathVariable @DecimalMin("-180.0") @DecimalMax("180.0") double lon) {
        IssPassResponse passes = issPositionService.getUpcomingPasses(lat, lon);
        return ResponseEntity.ok(passes);
    }
}