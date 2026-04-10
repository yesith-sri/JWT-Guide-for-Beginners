package com.edu.yesh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Entity
 *
 * Represents a user in the database
 *
 * @Entity - This class maps to 'users' table in database
 * @Table - Specifies table name
 * @Data - Lombok: generates getters, setters, toString, equals, hashCode
 * @Builder - Lombok: generates builder pattern for object creation
 * @NoArgsConstructor - Lombok: generates no-arg constructor
 * @AllArgsConstructor - Lombok: generates constructor with all fields
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Primary Key - Auto-incremented
     * @Id - Marks this as primary key
     * @GeneratedValue - Auto-increment value
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username - Unique identifier
     * @Column - Specifies column properties
     * unique = true - No two users can have same username
     * nullable = false - Cannot be empty
     * length = 50 - Maximum 50 characters
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Email - User's email address
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Password - Encrypted password (BCrypt hash)
     * We never store plain text passwords!
     * BCrypt hash is typically 60 characters
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Role - User's role for authorization
     * Examples: ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR
     * @Enumerated - Stores enum as string in database
     */
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Account active status
     * true = active, false = disabled/deleted
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * When user registered
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last update time
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Automatically set timestamps before saving
     * @PrePersist - Runs before INSERT
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Automatically update timestamp before updating
     * @PreUpdate - Runs before UPDATE
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
