package com.toolRent.backend.repositories;

import com.toolRent.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    /**
     * Find user by username for authentication (#29)
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Find all active users (#26, #27)
     */
    List<UserEntity> findByIsActive(Boolean isActive);

    /**
     * Find users by role (#27, #28)
     */
    List<UserEntity> findByRole(UserEntity.UserRole role);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if RUT (employee ID) exists
     */
    boolean existsByRut(String rut);
}
