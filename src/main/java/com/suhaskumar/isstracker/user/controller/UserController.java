package com.suhaskumar.isstracker.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for user preferences and data")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile() {
        // Placeholder for user profile logic
        return ResponseEntity.ok("User profile endpoint");
    }
}