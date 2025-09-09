package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.FeeEntity;
import com.toolRent.backend.repositories.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class FeeService{
    @Autowired
    private FeeRepository feeRepo;

    public FeeService(FeeRepository repo) {
        //super(repo);
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

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
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
		//throw new Exception(e.getMessage());
	}
	
    }
 
}
