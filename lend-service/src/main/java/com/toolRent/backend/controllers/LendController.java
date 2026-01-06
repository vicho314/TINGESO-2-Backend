package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.LendEntity;
import com.toolRent.backend.services.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                                                                 
@RequestMapping("/api/v1/lend")                                            
@CrossOrigin("*")
public class LendController {
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
	public boolean saveLend(@RequestBody LendEntity lend){
		boolean result = lendService.save(lend);
		return result;
	}

	@PutMapping("/")
	public boolean updateLend(@RequestBody LendEntity lend){
		boolean result = lendService.update(lend);
		return result;
	}

	@DeleteMapping("/{id}")
	public boolean deleteLend(@PathVariable Long id){
		boolean result = lendService.delete(id);
		return result;
	}
}
