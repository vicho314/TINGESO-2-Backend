package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.ToolEntity;
import com.toolRent.backend.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.stereotype.Service; 

import java.util.List;
import java.time.*;

@RestController                                                                 
@RequestMapping("/api/v1/tool")                                            
@CrossOrigin("*")
public class ToolController {
	//get,save,update,delete, getRut,getName, getAllBirthday
	@Autowired
	ToolService toolService;

	@GetMapping("/")
	public List<ToolEntity> listTools() {
		return toolService.getAll();
	}

	@GetMapping("/{id}")
	public ToolEntity getToolById(@PathVariable Long id){
		return toolService.getById(id);
	}	
	
	@PostMapping("/")
	public boolean saveTool(@RequestBody ToolEntity tool){
		boolean result = toolService.save(tool);
		return result;
	}

	@PutMapping("/")
	public boolean updateTool(@RequestBody ToolEntity tool){
		boolean result = toolService.update(tool);
		return result;
	}

	@DeleteMapping("/{id}")
	public boolean deleteTool(@PathVariable Long id){
		boolean result = toolService.delete(id);
		return result;
	}
}
