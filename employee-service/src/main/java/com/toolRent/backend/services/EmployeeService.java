package com.toolRent.backend.services;

import com.toolRent.backend.entities.EmployeeEntity;
import com.toolRent.backend.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class EmployeeService{
    @Autowired
    private EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository repo) {
	this.employeeRepo = repo;
    }
    
    public EmployeeEntity getById(Long id){
	return employeeRepo.findById(id).get();
    }

    public List<EmployeeEntity> getAll(){
	return (ArrayList<EmployeeEntity>) employeeRepo.findAll();
    }

    public boolean save(EmployeeEntity newEmployee){
        employeeRepo.save(newEmployee);
	return true;
    }

    public boolean update(EmployeeEntity newEmployee){
	employeeRepo.save(newEmployee);
	return true;
    }

    public boolean delete(Long id){
        try {
		employeeRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
	}
	
    }
 
}
