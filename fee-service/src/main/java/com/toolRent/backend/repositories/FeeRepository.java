package com.toolRent.backend.repositories;

import com.toolRent.backend.entities.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, Long> {

}
