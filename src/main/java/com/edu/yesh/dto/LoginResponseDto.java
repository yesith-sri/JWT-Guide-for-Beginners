package com.edu.yesh.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginResponseDto
 *
 * Data Transfer Object for login responses
 * Sent back to client after successful authentication
 *
 * @JsonInclude(JsonInclude.Include.NON_NULL)
 * - Doesn't include null fields in JSON response
 * - Example: if token is null, it won't be in JSON
 *
 * Client receives:
 * {
 *   "status": "200",
 *   "message": "Login successful",
 *   "token": "eyJhbGciOiJIUzUxMiJ9...",
 *   "type": "Bearer",
 *   "expiresIn": 86400
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto {

    /**
     * HTTP Status Code as String
     * Examples: "200", "400", "401", "500"
     */
    private String status;

    /**
     * Message for response
     * Examples: "Login successful", "Invalid credentials"
     */
    private String message;

    /**
     * JWT Token - Used in subsequent requests
     * Format: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0In0.xyz
     */
    private String token;

    /**
     * Token type - Always "Bearer"
     * Used in Authorization header: "Bearer <token>"
     */
    private String type;

    /**
     * Token expiration time
     * In seconds (can also be milliseconds)
     */
    private long expiresIn;
}