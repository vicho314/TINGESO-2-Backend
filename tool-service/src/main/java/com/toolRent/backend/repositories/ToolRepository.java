package com.toolRent.backend.repositories;

import com.toolRent.backend.entities.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {

}
