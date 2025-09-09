package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.LendEntity;
import com.toolRent.backend.repositories.LendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class LendService{
    @Autowired
    private LendRepository lendRepo;

    public LendService(LendRepository repo) {
        //super(repo);
	this.lendRepo = repo;
    }
    
    public LendEntity getById(Long id){
	return lendRepo.findById(id).get();
    }

    public List<LendEntity> getAll(){
	return (ArrayList<LendEntity>) lendRepo.findAll();
    }

    public boolean save(LendEntity newLend){
        lendRepo.save(newLend);
	return true;
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
    public boolean update(LendEntity newLend){
	lendRepo.save(newLend);
	return true;
    }

    public boolean delete(Long id){
        try {
		lendRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
		//throw new Exception(e.getMessage());
	}
	
    }
 
}
