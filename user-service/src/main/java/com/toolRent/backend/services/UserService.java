package com.toolRent.backend.services;

import com.toolRent.backend.entities.UserEntity;
import com.toolRent.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * M7 - User and Roles Management Service
 * Handles: User registration, role assignment, authentication, authorization, session control
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * M7 - #26: Register users into the system with access credentials
     */
    public boolean registerUser(UserEntity newUser) {
        try {
            // Validate user data
            if (!newUser.isValidUser()) {
                return false;
            }

            // Check if username already exists
            if (userRepository.existsByUsername(newUser.getUsername())) {
                return false;
            }

            // Check if RUT already exists
            if (userRepository.existsByRut(newUser.getRut())) {
                return false;
            }

            // In production, password should be hashed
            // For now, we'll just save it as is
            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * M7 - #27: Assign user roles (Admin, Employee)
     */
    public boolean assignUserRole(Long userId, UserEntity.UserRole role) {
        try {
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return false;
            }

            UserEntity user = userOpt.get();
            user.setRole(role);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * M7 - #28: Validate permissions according to role
     */
    public boolean validatePermission(Long userId, UserEntity.UserRole requiredRole) {
        try {
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return false;
            }

            UserEntity user = userOpt.get();

            // Check if user is active
            if (!user.getIsActive()) {
                return false;
            }

            // Check role permission
            return user.hasRole(requiredRole);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * M7 - #29: Auth by login and session control
     */
    public Optional<UserEntity> authenticateUser(String username, String password) {
        try {
            Optional<UserEntity> userOpt = userRepository.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }

            UserEntity user = userOpt.get();

            // Check if user is active
            if (!user.getIsActive()) {
                return Optional.empty();
            }

            // Validate password (in production, should use hashed comparison)
            if (!user.getPassword().equals(password)) {
                return Optional.empty();
            }

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Get user by ID
     */
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get all users
     */
    public List<UserEntity> getAllUsers() {
        return (ArrayList<UserEntity>) userRepository.findAll();
    }

    /**
     * Get all active users
     */
    public List<UserEntity> getActiveUsers() {
        return userRepository.findByIsActive(true);
    }

    /**
     * Get users by role (#27)
     */
    public List<UserEntity> getUsersByRole(UserEntity.UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Update user
     */
    public boolean updateUser(UserEntity updatedUser) {
        try {
            if (!userRepository.existsById(updatedUser.getId())) {
                return false;
            }
            userRepository.save(updatedUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Deactivate user
     */
    public boolean deactivateUser(Long userId) {
        try {
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return false;
            }

            UserEntity user = userOpt.get();
            user.setIsActive(false);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete user
     */
    public boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
