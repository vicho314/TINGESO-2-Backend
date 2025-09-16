package com.toolRent.backend.controllers;

import com.toolRent.backend.entities.UserEntity;
import com.toolRent.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.stereotype.Service; 

import java.util.List;
import java.time.*;

@RestController                                                                 
@RequestMapping("/api/v1/user")                                            
@CrossOrigin("*")
public class UserController {
	//get,save,update,delete, getRut,getName, getAllBirthday
	@Autowired
	UserService userService;

	@GetMapping("/")
	public List<UserEntity> listUsers() {
		return userService.getAll();
	}

	@GetMapping("/{id}")
	public UserEntity getUserById(@PathVariable Long id){
		return userService.getById(id);
	}	
	
	@PostMapping("/")
	public boolean saveUser(@RequestBody UserEntity user){
		boolean result = userService.save(user);
		return result;
	}

	@PutMapping("/")
	public boolean updateUser(@RequestBody UserEntity user){
		boolean result = userService.update(user);
		return result;
	}

	@DeleteMapping("/{id}")
	public boolean deleteUser(@PathVariable Long id){
		boolean result = userService.delete(id);
		return result;
	}
}
