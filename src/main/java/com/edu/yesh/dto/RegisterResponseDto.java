package com.edu.yesh.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegisterResponseDto
 *
 * Response sent after registration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponseDto {

    /**
     * Status code
     */
    private String status;

    /**
     * Response message
     */
    private String message;

    /**
     * Registered user's ID
     */
    private Long userId;

    /**
     * Registered username
     */
    private String username;

    /**
     * Registered email
     */
    private String email;
}