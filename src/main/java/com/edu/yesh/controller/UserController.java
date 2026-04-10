package com.edu.yesh.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * UserController
 *
 * Protected endpoints that require JWT authentication
 *
 * These endpoints can ONLY be accessed with valid JWT token
 * in Authorization header: Authorization: Bearer <token>
 *
 * @RestController - Handles REST requests
 * @RequestMapping("/users") - Base URL: /api/users
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    /**
     * Get Current User Profile
     *
     * URL: GET /api/users/profile
     *
     * Requires: Valid JWT token
     * Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
     *
     * Response (200 OK):
     * {
     *   "success": true,
     *   "message": "Profile retrieved successfully",
     *   "username": "john_doe",
     *   "email": "john@example.com"
     * }
     *
     * Response without token (401 Unauthorized):
     * {
     *   Error 401
     * }
     *
     * Principal:
     * - Spring Security injects authenticated user info
     * - principal.getName() gives username
     * - Available because JWT is validated by filter
     *
     * @param principal - Authenticated user (injected by Spring)
     * @return User profile info
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(Principal principal) {

        // Get username from authenticated user
        String username = principal.getName();

        log.info("Profile requested for user: {}", username);

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profile retrieved successfully");
        response.put("username", username);
        response.put("email", username + "@example.com");  // Dummy email

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Admin Only Endpoint
     *
     * URL: GET /api/users/admin/dashboard
     *
     * Requires:
     * 1. Valid JWT token
     * 2. User must have ROLE_ADMIN
     *
     * @PreAuthorize("hasRole('ADMIN')")
     * - Checks if authenticated user has ADMIN role
     * - If not: returns 403 Forbidden
     * - If yes: allows access
     *
     * How it works:
     * 1. User sends JWT token
     * 2. Filter extracts authorities from JWT
     * 3. @PreAuthorize checks if ROLE_ADMIN is present
     * 4. If yes → execute method
     * 5. If no → 403 Forbidden
     *
     * Response success (200 OK):
     * {
     *   "success": true,
     *   "message": "Admin dashboard data",
     *   "adminUser": "admin"
     * }
     *
     * Response without ADMIN role (403 Forbidden):
     * {
     *   Error 403: Access Denied
     * }
     *
     * @param principal - Authenticated admin user
     * @return Admin dashboard data
     */
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminDashboard(
            Principal principal) {

        String username = principal.getName();
        log.info("Admin dashboard accessed by: {}", username);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Admin dashboard data");
        response.put("adminUser", username);
        response.put("totalUsers", 100);  // Dummy data
        response.put("activeUsers", 85);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Moderator/Admin Endpoint
     *
     * @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
     * - Allows users with EITHER ADMIN or MODERATOR role
     * - Multiple roles can be checked
     *
     * URL: GET /api/users/moderator/panel
     *
     * @param principal - Authenticated user (admin or moderator)
     * @return Moderator panel data
     */
    @GetMapping("/moderator/panel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Map<String, Object>> getModeratorPanel(
            Principal principal) {

        String username = principal.getName();
        log.info("Moderator panel accessed by: {}", username);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Moderator panel data");
        response.put("user", username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}