package com.edu.yesh.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegisterRequestDto
 *
 * Data Transfer Object (DTO) for user registration requests
 *
 * What is a DTO?
 * DTO is a simple object used to transfer data between client and server
 * It doesn't contain business logic, just holds data
 *
 * Why use DTO?
 * 1. Validates input from client
 * 2. Prevents direct exposure of entity classes
 * 3. Can receive different fields than entity
 * 4. Cleaner API contracts
 *
 * Validation:
 * This class uses Jakarta Validation (formerly javax.validation)
 * When @Valid annotation is used on controller parameter,
 * these validations are automatically checked
 *
 * If validation fails:
 * - MethodArgumentNotValidException is thrown
 * - GlobalExceptionHandler can catch and handle it
 * - Response 400 Bad Request with error details
 *
 * @Data - Lombok annotation
 *         Generates: getters, setters, toString(), equals(), hashCode()
 *
 * @NoArgsConstructor - Lombok annotation
 *                      Generates constructor with no arguments
 *                      Example: RegisterRequestDto dto = new RegisterRequestDto();
 *
 * @AllArgsConstructor - Lombok annotation
 *                       Generates constructor with all fields as arguments
 *                       Example: RegisterRequestDto dto = new RegisterRequestDto("john", "john@example.com", "pass");
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    /**
     * Username field
     *
     * Validations applied:
     * 1. @NotBlank - Cannot be null, empty, or just whitespace
     *    Error message: "Username is required"
     *
     * 2. @Size - String length must be between min and max
     *    min = 3 - Minimum 3 characters (e.g., "abc" is valid, "ab" is invalid)
     *    max = 50 - Maximum 50 characters
     *    Error message: "Username must be between 3 and 50 characters"
     *
     * Valid examples:
     * - "john_doe" ✅
     * - "admin123" ✅
     * - "john.smith.2024" ✅
     *
     * Invalid examples:
     * - "" ❌ (empty)
     * - "  " ❌ (only whitespace)
     * - "ab" ❌ (too short - less than 3)
     * - null ❌ (null value)
     * - (51+ characters) ❌ (too long - more than 50)
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * Email field
     *
     * Validations applied:
     * 1. @NotBlank - Cannot be null or empty
     *    Error message: "Email is required"
     *
     * 2. @Email - Must be valid email format
     *    Checks format: something@something.something
     *    Error message: "Email should be valid"
     *
     * Email validation checks:
     * - Contains @ symbol
     * - Has domain name before @
     * - Has domain extension after @
     * - No spaces
     * - Valid characters
     *
     * Valid examples:
     * - "john@example.com" ✅
     * - "user.name@company.co.uk" ✅
     * - "admin+tag@domain.org" ✅
     *
     * Invalid examples:
     * - "" ❌ (empty)
     * - "john" ❌ (no @ symbol)
     * - "john@" ❌ (no domain)
     * - "@example.com" ❌ (no username)
     * - "john @example.com" ❌ (contains space)
     * - null ❌ (null value)
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * Password field
     *
     * Validations applied:
     * 1. @NotBlank - Cannot be null or empty
     *    Error message: "Password is required"
     *
     * 2. @Size - Length must be between min and max
     *    min = 6 - Minimum 6 characters (basic security requirement)
     *    max = 100 - Maximum 100 characters (BCrypt hash will fit)
     *    Error message: "Password must be between 6 and 100 characters"
     *
     * Why minimum 6 characters?
     * - Prevents weak passwords
     * - Standard security requirement
     * - Should be higher in production (8+ recommended)
     *
     * Valid examples:
     * - "password123" ✅
     * - "MySecure@Pass2024" ✅
     * - "secure123" ✅
     *
     * Invalid examples:
     * - "pass" ❌ (too short - less than 6)
     * - "12345" ❌ (too short - less than 6)
     * - "" ❌ (empty)
     * - null ❌ (null value)
     * - "   " ❌ (only whitespace - treated as empty by @NotBlank)
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
