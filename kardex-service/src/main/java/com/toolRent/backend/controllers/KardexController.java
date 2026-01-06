package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.KardexEntity;
import com.toolRent.backend.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
