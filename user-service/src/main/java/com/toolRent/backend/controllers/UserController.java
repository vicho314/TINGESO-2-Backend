package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.UserEntity;
import com.toolRent.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * M7 - User and Roles Management Controller
 * Endpoints for user registration, authentication, role assignment, and authorization
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * M7 - #26: Register users into the system with access credentials
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity newUser) {
        if (userService.registerUser(newUser)) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.badRequest().body("Failed to register user");
    }

    /**
     * M7 - #29: Auth by login and session control
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam String username,
            @RequestParam String password) {
        
        Optional<UserEntity> userOpt = userService.authenticateUser(username, password);
        
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    /**
     * M7 - #27: Assign user roles (Admin, Employee)
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<String> assignRole(
            @PathVariable Long userId,
            @RequestParam UserEntity.UserRole role) {
        
        if (userService.assignUserRole(userId, role)) {
            return ResponseEntity.ok("Role assigned successfully");
        }
        return ResponseEntity.badRequest().body("Failed to assign role");
    }

    /**
     * M7 - #28: Validate permissions according to role
     */
    @GetMapping("/{userId}/validate-permission")
    public ResponseEntity<Boolean> validatePermission(
            @PathVariable Long userId,
            @RequestParam UserEntity.UserRole requiredRole) {
        
        boolean hasPermission = userService.validatePermission(userId, requiredRole);
        return ResponseEntity.ok(hasPermission);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get all users
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get all active users
     */
    @GetMapping("/active")
    public ResponseEntity<List<UserEntity>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    /**
     * Get users by role
     */
    @GetMapping("/by-role")
    public ResponseEntity<List<UserEntity>> getUsersByRole(
            @RequestParam UserEntity.UserRole role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserEntity updatedUser) {
        
        updatedUser.setId(id);
        if (userService.updateUser(updatedUser)) {
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.badRequest().body("Failed to update user");
    }

    /**
     * Deactivate user
     */
    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        if (userService.deactivateUser(id)) {
            return ResponseEntity.ok("User deactivated successfully");
        }
        return ResponseEntity.badRequest().body("Failed to deactivate user");
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.badRequest().body("Failed to delete user");
    }
}
