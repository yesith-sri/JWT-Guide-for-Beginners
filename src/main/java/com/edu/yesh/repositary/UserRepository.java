package com.edu.yesh.repositary;

import com.edu.yesh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * UserRepository
 *
 * JPA Repository for User entity
 * Provides database CRUD operations (Create, Read, Update, Delete)
 *
 * JpaRepository automatically provides:
 * - save(entity) - Insert or update
 * - delete(entity) - Delete
 * - findById(id) - Find by primary key
 * - findAll() - Get all records
 * - deleteById(id) - Delete by ID
 *
 * We add custom query methods for our specific needs
 * Spring Data JPA automatically implements these by analyzing method name!
 *
 * @Repository - Marks this as Spring Repository (DAO pattern)
 * <User, Long> - Entity type and primary key type
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     *
     * Method name format: findBy<FieldName>
     * Spring Data JPA generates SQL: SELECT * FROM users WHERE username = ?
     *
     * Returns Optional because user might not exist
     * Optional is Java's way of handling null values safely
     *
     * Usage:
     * Optional<User> user = userRepository.findByUsername("john_doe");
     * if (user.isPresent()) {
     *     User u = user.get();
     * }
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     *
     * SQL: SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if username exists
     *
     * Method name: existsBy<FieldName>
     * Spring Data JPA generates SQL: SELECT COUNT(*) FROM users WHERE username = ?
     * Returns: true if exists, false if not
     *
     * Usage:
     * if (userRepository.existsByUsername("john_doe")) {
     *     // Username already taken
     * }
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     *
     * SQL: SELECT COUNT(*) FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);
}