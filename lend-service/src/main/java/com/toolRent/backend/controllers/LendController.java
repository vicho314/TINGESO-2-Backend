package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.LendEntity;
import com.toolRent.backend.services.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

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
	
	@PostMapping("/create")
	public boolean createLending(@RequestBody LendEntity lend){
		return lendService.createLending(lend);
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
	
	@GetMapping("/{id}/late-fees")
	public Double getLateFees(@PathVariable Long id) {
		return lendService.calculateLateFees(id);
	}
	
	@GetMapping("/client/{clientId}/is-blocked")
	public boolean isClientBlocked(@PathVariable Long clientId) {
		return lendService.isClientBlocked(clientId);
	}
	
	@GetMapping("/client/{clientId}/lendings")
	public List<LendEntity> getClientLendings(@PathVariable Long clientId) {
		return lendService.getClientLendings(clientId);
	}
	
	@PutMapping("/{id}/return")
	public boolean returnLending(@PathVariable Long id) {
		return lendService.returnLending(id);
	}
	
	@GetMapping("/active-lendings")
	public List<LendEntity> getActiveLendings() {
		return (ArrayList<LendEntity>) lendService.getAll()
			.stream()
			.filter(lend -> lend.getState() == 'A')
			.toList();
	}
}
