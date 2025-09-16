package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.EmployeeEntity;
import com.toolRent.backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.stereotype.Service; 

import java.util.List;
import java.time.*;

@RestController                                                                 
@RequestMapping("/api/v1/employee")                                            
@CrossOrigin("*")
public class EmployeeController {
	//get,save,update,delete, getRut,getName, getAllBirthday
	@Autowired
	EmployeeService employeeService;

	@GetMapping("/")
	public List<EmployeeEntity> listEmployees() {
		return employeeService.getAll();
	}

	@GetMapping("/{id}")
	public EmployeeEntity getEmployeeById(@PathVariable Long id){
		return employeeService.getById(id);
	}	
	
	@PostMapping("/")
	public boolean saveEmployee(@RequestBody EmployeeEntity employee){
		boolean result = employeeService.save(employee);
		return result;
	}

	@PutMapping("/")
	public boolean updateEmployee(@RequestBody EmployeeEntity employee){
		boolean result = employeeService.update(employee);
		return result;
	}

	@DeleteMapping("/{id}")
	public boolean deleteEmployee(@PathVariable Long id){
		boolean result = employeeService.delete(id);
		return result;
	}
}
