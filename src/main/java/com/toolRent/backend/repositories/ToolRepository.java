package com.toolRent.backend.repositories;


import com.toolRent.backend.entities.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {
	List<ToolEntity> findAllByCategory(String category);
	//FIXME: test this thoroughly
	@Query("SELECT DISTINCT a.category FROM ToolEntity a ")
	List<String> findDistinctCategory();
}
