package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.FeeEntity;
import com.toolRent.backend.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                                                                 
@RequestMapping("/api/v1/fee")                                            
@CrossOrigin("*")
public class FeeController {
	@Autowired
	FeeService feeService;

	@GetMapping("/")
	public List<FeeEntity> listFees() {
		return feeService.getAll();
	}

	@GetMapping("/{id}")
	public FeeEntity getFeeById(@PathVariable Long id){
		return feeService.getById(id);
	}	
	
	@PostMapping("/")
	public boolean saveFee(@RequestBody FeeEntity fee){
		boolean result = feeService.save(fee);
		return result;
	}

	@PutMapping("/")
	public boolean updateFee(@RequestBody FeeEntity fee){
		boolean result = feeService.update(fee);
		return result;
	}

	@DeleteMapping("/{id}")
	public boolean deleteFee(@PathVariable Long id){
		boolean result = feeService.delete(id);
		return result;
	}
	
	@GetMapping("/daily-lending-fee")
	public Double getDailyLendingFee() {
		return feeService.getDailyLendingFee();
	}
	
	@GetMapping("/daily-late-fee")
	public Double getDailyLateFee() {
		return feeService.getDailyLateFee();
	}
	
	@GetMapping("/calculate-late-fee")
	public Double calculateLateFee(
		@RequestParam Long toolId,
		@RequestParam Integer daysLate) {
		return feeService.calculateLateFee(toolId, daysLate);
	}
	
	@PutMapping("/daily-lending-fee")
	public boolean setDailyLendingFee(
		@RequestParam Long userId,
		@RequestParam Double amount) {
		return feeService.setDailyLendingFee(userId, amount);
	}
	
	@PutMapping("/daily-late-fee")
	public boolean setDailyLateFee(
		@RequestParam Long userId,
		@RequestParam Double amount) {
		return feeService.setDailyLateFee(userId, amount);
	}
	
	@PutMapping("/replacement-value/{toolId}")
	public boolean setReplacementValue(
		@RequestParam Long userId,
		@PathVariable Long toolId,
		@RequestParam Integer value) {
		return feeService.setReplacementValue(userId, toolId, value);
	}
	
	@GetMapping("/replacement-value/{toolId}")
	public Integer getReplacementValue(@PathVariable Long toolId) {
		return feeService.getReplacementValue(toolId);
	}
	
	@GetMapping("/tool/{toolId}/all")
	public List<FeeEntity> getToolFees(@PathVariable Long toolId) {
		return feeService.getToolFees(toolId);
	}
}
