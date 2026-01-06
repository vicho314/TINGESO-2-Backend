package com.toolRent.backend.repositories;

import com.toolRent.backend.entities.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity, Long> {

}
