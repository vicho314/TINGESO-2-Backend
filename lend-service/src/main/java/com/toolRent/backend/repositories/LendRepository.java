package com.toolRent.backend.repositories;

import com.toolRent.backend.entities.LendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LendRepository extends JpaRepository<LendEntity, Long> {

}
