package com.toolRent.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * M7 - User and Roles Management: Employee/User entity
 * Handles both employee registration and user authentication
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee information (from original EmployeeEntity)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mail;

    @Column(nullable = false)
    private String rut;

    // Authentication fields (#26, #29)
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // Should be hashed in production

    // Role assignment (#27)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // Admin or Employee

    // Session control (#29)
    @Column
    private LocalDateTime lastLogin;

    @Column
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Enum for user roles (#27, #28)
     * Admin: total system access
     * Employee: only lends/returns and reports
     */
    public enum UserRole {
        ADMIN,      // Total system access
        EMPLOYEE    // Limited access: lends/returns and reports only
    }

    /**
     * Check if user has required role (#28)
     */
    public boolean hasRole(UserRole requiredRole) {
        return this.role == requiredRole || this.role == UserRole.ADMIN;
    }

    /**
     * Validate user credentials
     */
    public boolean isValidUser() {
        return name != null && !name.isEmpty() &&
               mail != null && !mail.isEmpty() &&
               rut != null && !rut.isEmpty() &&
               username != null && !username.isEmpty() &&
               password != null && !password.isEmpty() &&
               isActive;
    }
}
