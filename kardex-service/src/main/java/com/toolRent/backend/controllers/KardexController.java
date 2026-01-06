package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.KardexEntity;
import com.toolRent.backend.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController                                                                 
@RequestMapping("/api/v1/kardex")                                            
@CrossOrigin("*")
public class KardexController {
	@Autowired
	KardexService kardexService;

	@GetMapping("/")
	public List<KardexEntity> listKardex() {
		return kardexService.getAll();
	}

	@GetMapping("/{id}")
	public KardexEntity getKardexById(@PathVariable Long id){
		return kardexService.getById(id);
	}	
	
	@PostMapping("/")
	public boolean saveKardex(@RequestBody KardexEntity kardex){
		boolean result = kardexService.save(kardex);
		return result;
	}

	@PutMapping("/")
	public boolean updateKardex(@RequestBody KardexEntity kardex){
		boolean result = kardexService.update(kardex);
		return result;
	}

	@DeleteMapping("/{id}")
	public boolean deleteKardex(@PathVariable Long id){
		boolean result = kardexService.delete(id);
		return result;
	}
	
	@PostMapping("/register-movement")
	public boolean registerMovement(@RequestBody KardexEntity movement) {
		return kardexService.registerMovement(movement);
	}
	
	@PostMapping("/register")
	public boolean registerMovementDetails(
		@RequestParam Long toolId,
		@RequestParam(required = false) Long lendId,
		@RequestParam String movementType,
		@RequestParam(required = false) String description,
		@RequestParam(required = false) Long createdBy) {
		return kardexService.registerMovement(toolId, lendId, movementType, description, createdBy);
	}
	
	@GetMapping("/tool/{toolId}/history")
	public List<KardexEntity> getToolHistory(@PathVariable Long toolId) {
		return kardexService.getToolHistory(toolId);
	}
	
	@GetMapping("/lending/{lendId}/history")
	public List<KardexEntity> getLendingHistory(@PathVariable Long lendId) {
		return kardexService.getLendingHistory(lendId);
	}
	
	@GetMapping("/date-range")
	public List<KardexEntity> getMovementsByDateRange(
		@RequestParam LocalDate startDate,
		@RequestParam LocalDate endDate) {
		return kardexService.getMovementsByDateRange(startDate, endDate);
	}
	
	@GetMapping("/tool/{toolId}/date-range")
	public List<KardexEntity> getToolMovementsByDateRange(
		@PathVariable Long toolId,
		@RequestParam LocalDate startDate,
		@RequestParam LocalDate endDate) {
		return kardexService.getToolMovementsByDateRange(toolId, startDate, endDate);
	}
	
	@GetMapping("/type/{movementType}")
	public List<KardexEntity> getMovementsByType(@PathVariable String movementType) {
		return kardexService.getMovementsByType(movementType);
	}
	
	@GetMapping("/recent")
	public List<KardexEntity> getRecentMovements(
		@RequestParam(defaultValue = "50") int limit) {
		return kardexService.getRecentMovements(limit);
	}
}
