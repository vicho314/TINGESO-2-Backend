package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.LendEntity;
import com.toolRent.backend.services.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.stereotype.Service; 

import java.util.List;
import java.time.*;

@RestController                                                                 
@RequestMapping("/api/v1/lend")                                            
@CrossOrigin("*")
public class LendController {
	//get,save,update,delete, getRut,getName, getAllBirthday
	@Autowired
	LendService lendService;

	@GetMapping("/")
	public List<LendEntity> listLends() {
		return lendService.getAll();
	}

	@GetMapping("/{id}")
	public LendEntity getLendById(@PathVariable Long id){
		return lendService.getById(id);
	}	
	
	@PostMapping("/")
	public LendEntity saveLend(@RequestBody LendEntity lend){
		LendEntity result = lendService.save(lend);
		return result;
	}

	@PutMapping("/")
	public LendEntity updateLend(@RequestBody LendEntity lend){
		LendEntity result = lendService.update(lend);
		return result;
	}

	@DeleteMapping("/{id}")
	public boolean deleteLend(@PathVariable Long id){
		boolean result = lendService.delete(id);
		return result;
	}
}
