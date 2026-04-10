package com.edu.yesh.controller;
import com.edu.yesh.dto.LoginRequestDto;
import com.edu.yesh.dto.LoginResponseDto;
import com.edu.yesh.dto.RegisterRequestDto;
import com.edu.yesh.dto.RegisterResponseDto;
import com.edu.yesh.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 *
 * REST API endpoints for authentication
 *
 * Provides:
 * - User registration
 * - User login (with JWT token)
 *
 * Endpoints:
 * - POST /api/auth/register
 * - POST /api/auth/login
 *
 * @RestController - This class handles REST requests
 * @RequestMapping("/auth") - Base URL for this controller
 * @RequiredArgsConstructor - Auto-generates constructor
 * @Slf4j - Logger
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    /**
     * Authentication Service
     * Injected by Spring via constructor
     */
    private final AuthService authService;

    /**
     * Register Endpoint
     *
     * URL: POST /api/auth/register
     * Content-Type: application/json
     *
     * Request body example:
     * {
     *   "username": "john_doe",
     *   "email": "john@example.com",
     *   "password": "password123"
     * }
     *
     * Response success (201 Created):
     * {
     *   "status": "201",
     *   "message": "User registered successfully",
     *   "userId": 1,
     *   "username": "john_doe",
     *   "email": "john@example.com"
     * }
     *
     * Response failure (400 Bad Request):
     * {
     *   "status": "400",
     *   "message": "Username already exists"
     * }
     *
     * @Valid - Validates input according to annotations
     *         If validation fails, MethodArgumentNotValidException is thrown
     * @RequestBody - Converts JSON to RegisterRequestDto object
     *
     * @param registerRequestDto - Registration data from request body
     * @return ResponseEntity with status and response body
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(
            @Valid @RequestBody RegisterRequestDto registerRequestDto) {

        log.info("Register request received for username: {}",
                registerRequestDto.getUsername());

        // Call service to register user
        RegisterResponseDto response = authService.register(registerRequestDto);

        // Return appropriate HTTP status
        if ("201".equals(response.getStatus())) {
            // 201 Created - User successfully registered
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            // 400 Bad Request - Registration failed
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Login Endpoint
     *
     * URL: POST /api/auth/login
     * Content-Type: application/json
     *
     * Request body example:
     * {
     *   "username": "john_doe",
     *   "password": "password123"
     * }
     *
     * Response success (200 OK):
     * {
     *   "status": "200",
     *   "message": "Login successful",
     *   "token": "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJWb3RpbmcgU3lzdGVtIiwic3ViIjoiSnd0IFRva2VuIiwidXNlcm5hbWUiOiJqb2huX2RvZSIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjk5MDAwMDAwLCJleHAiOjE2OTkwODY0MDB9.xyz",
     *   "type": "Bearer",
     *   "expiresIn": 86400
     * }
     *
     * Response failure (401 Unauthorized):
     * {
     *   "status": "401",
     *   "message": "Invalid username or password"
     * }
     *
     * How to use token:
     * Add to Authorization header in subsequent requests:
     * Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
     *
     * @param loginRequestDto - Username and password
     * @return ResponseEntity with JWT token or error message
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        log.info("Login request received for username: {}",
                loginRequestDto.getUsername());

        // Call service to login user
        LoginResponseDto response = authService.login(loginRequestDto);

        // Return appropriate HTTP status
        if ("200".equals(response.getStatus())) {
            // 200 OK - Login successful, token provided
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if ("401".equals(response.getStatus())) {
            // 401 Unauthorized - Invalid credentials
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else {
            // 500 Internal Server Error
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}