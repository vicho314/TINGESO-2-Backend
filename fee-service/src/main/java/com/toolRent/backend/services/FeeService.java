package com.toolRent.backend.services;

import com.toolRent.backend.entities.FeeEntity;
import com.toolRent.backend.repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class FeeService{
    @Autowired
    private FeeRepository feeRepo;

    public FeeService(FeeRepository repo) {
	this.feeRepo = repo;
    }
    
    public FeeEntity getById(Long id){
	return feeRepo.findById(id).get();
    }

    public List<FeeEntity> getAll(){
	return (ArrayList<FeeEntity>) feeRepo.findAll();
    }

    public boolean save(FeeEntity newFee){
        feeRepo.save(newFee);
	return true;
    }

    public boolean update(FeeEntity newFee){
	feeRepo.save(newFee);
	return true;
    }

    public boolean delete(Long id){
        try {
		feeRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
	}
	
    }
 
}
