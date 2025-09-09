package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.FeeEntity;
import com.toolRent.backend.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.stereotype.Service; 

import java.util.List;
import java.time.*;

@RestController                                                                 
@RequestMapping("/api/v1/fee")                                            
@CrossOrigin("*")
public class FeeController {
	//get,save,update,delete, getRut,getName, getAllBirthday
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
}
