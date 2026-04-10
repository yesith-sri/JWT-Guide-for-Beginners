package com.edu.yesh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * LoginRequestDto
 *
 * Data Transfer Object for login requests
 *
 * Client sends:
 * {
 *   "username": "john_doe",
 *   "password": "password123"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    /**
     * Username for login
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * Password for login
     */
    @NotBlank(message = "Password is required")
    private String password;
}