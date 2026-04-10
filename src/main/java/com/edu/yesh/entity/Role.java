package com.edu.yesh.entity;

/**
 * Role Enum
 *
 * Defines different user roles in the system
 * Used for authorization - checking what user can do
 *
 * Spring Security convention: prepend "ROLE_" to authority names
 */
public enum Role {
    /**
     * Regular user - basic access
     * In Spring Security becomes: ROLE_USER
     */
    ROLE_USER,

    /**
     * Administrator - full access
     * In Spring Security becomes: ROLE_ADMIN
     */
    ROLE_ADMIN,

    /**
     * Moderator - limited admin access
     * In Spring Security becomes: ROLE_MODERATOR
     */
    ROLE_MODERATOR

}