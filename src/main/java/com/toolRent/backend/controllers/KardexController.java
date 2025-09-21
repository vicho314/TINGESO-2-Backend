package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.KardexEntity;
import com.toolRent.backend.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.stereotype.Service; 

import java.util.List;
import java.time.*;

@RestController                                                                 
@RequestMapping("/api/v1/kardex")                                            
@CrossOrigin("*")
public class KardexController {
	//get,save,update,delete, getRut,getName, getAllBirthday
	@Autowired
	KardexService kardexService;

	@GetMapping("/")
	public List<KardexEntity> listKardexs() {
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

	@PostMapping("/saveTool/")
	public KardexEntity saveTool(@RequestBody KardexEntity kardex){
		KardexEntity result = kardexService.saveTool(kardex);
		return result;
	}
	
	@PostMapping("/takeDownTool/")
	public KardexEntity takeDownTool(@RequestBody KardexEntity kardex){
		KardexEntity result = kardexService.takeDownTool(kardex);
		return result;
	}
	
	@PostMapping("/repairTool/")
	public KardexEntity repairTool(@RequestBody KardexEntity kardex){
		KardexEntity result = kardexService.repairTool(kardex);
		return result;
	}

}
