package com.edu.yesh.service;

import com.edu.yesh.dto.LoginRequestDto;
import com.edu.yesh.dto.LoginResponseDto;
import com.edu.yesh.dto.RegisterRequestDto;
import com.edu.yesh.dto.RegisterResponseDto;
import com.edu.yesh.entity.Role;
import com.edu.yesh.entity.User;
import com.edu.yesh.repositary.UserRepository;
import com.edu.yesh.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService
 *
 * Business logic for authentication and registration
 *
 * Responsibilities:
 * 1. Register new users
 * 2. Authenticate existing users
 * 3. Validate business rules
 * 4. Generate JWT tokens
 *
 * @Service - Marks this as a service class (business logic layer)
 * @Transactional - Each method runs in a database transaction
 * @RequiredArgsConstructor - Lombok: generates constructor for final fields
 * @Slf4j - Provides logger
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    /**
     * User Repository - for database operations
     */
    private final UserRepository userRepository;

    /**
     * Password Encoder - for hashing passwords
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Authentication Manager - for authenticating users
     */
    private final AuthenticationManager authenticationManager;

    /**
     * JWT Token Provider - for generating JWT tokens
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT Expiration time - from application.yml
     */
    @Value("${app.jwtExpiration}")
    private long jwtExpiration;

    /**
     * Register new user
     *
     * Process:
     * 1. Validate input (username, email don't exist)
     * 2. Create new User entity
     * 3. Hash password using BCrypt
     * 4. Set role to USER
     * 5. Save to database
     * 6. Return success response
     *
     * @param registerRequestDto - Registration data from client
     * @return RegisterResponseDto - Registration response
     */
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {

        log.info("Registration request for username: {}",
                registerRequestDto.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            log.warn("Registration failed: Username already exists - {}",
                    registerRequestDto.getUsername());

            return RegisterResponseDto.builder()
                    .status("400")
                    .message("Username already exists")
                    .build();
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            log.warn("Registration failed: Email already exists - {}",
                    registerRequestDto.getEmail());

            return RegisterResponseDto.builder()
                    .status("400")
                    .message("Email already exists")
                    .build();
        }

        // Create new User entity
        // We use @Builder annotation for clean object creation
        User user = User.builder()
                .username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail())
                // IMPORTANT: Encrypt password using BCrypt!
                // Never store plain text passwords!
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(Role.ROLE_USER)  // Default role for new users
                .active(true)           // Account is active by default
                .build();

        // Save user to database
        User savedUser = userRepository.save(user);

        log.info("User registered successfully: {}", savedUser.getUsername());

        // Return success response
        return RegisterResponseDto.builder()
                .status("201")
                .message("User registered successfully")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }

    /**
     * Login user and generate JWT token
     *
     * Process:
     * 1. Create unauthenticated Authentication token with username & password
     * 2. Call authenticationManager.authenticate()
     *    - Finds user by username
     *    - Checks if password matches (using BCrypt)
     *    - Returns authenticated Authentication object
     *    - Throws AuthenticationException if failed
     * 3. Generate JWT token from authenticated user
     * 4. Return token in response
     *
     * @param loginRequestDto - Login credentials from client
     * @return LoginResponseDto - JWT token and user info
     */
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        log.info("Login request for username: {}",
                loginRequestDto.getUsername());

        try {
            // Step 1: Create unauthenticated token
            // This token contains username and password from request
            // It's NOT authenticated yet
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    );

            // Step 2: Authenticate the user
            // authenticationManager will:
            // 1. Load user by username from database
            // 2. Check if password matches (BCrypt comparison)
            // 3. If successful: return authenticated Authentication object
            // 4. If failed: throw AuthenticationException
            Authentication authenticatedUser =
                    authenticationManager.authenticate(authentication);

            // Step 3: Generate JWT token
            // The token contains username and authorities
            String token = jwtTokenProvider.generateToken(authenticatedUser);

            log.info("User logged in successfully: {}",
                    loginRequestDto.getUsername());

            // Step 4: Return success response with token
            return LoginResponseDto.builder()
                    .status("200")
                    .message("Login successful")
                    .token(token)                           // JWT token
                    .type("Bearer")                         // Token type
                    .expiresIn(jwtExpiration / 1000)        // Convert ms to seconds
                    .build();

        } catch (AuthenticationException e) {
            // Authentication failed - invalid username or password
            log.error("Authentication failed for user: {} - {}",
                    loginRequestDto.getUsername(), e.getMessage());

            return LoginResponseDto.builder()
                    .status("401")
                    .message("Invalid username or password")
                    .build();

        } catch (Exception e) {
            // Any other error
            log.error("Login error: {}", e.getMessage(), e);

            return LoginResponseDto.builder()
                    .status("500")
                    .message("Internal server error")
                    .build();
        }
    }
}