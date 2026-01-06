package com.toolRent.backend.services;

import com.toolRent.backend.entities.LendEntity;
import com.toolRent.backend.repositories.LendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class LendService{
    @Autowired
    private LendRepository lendRepo;

    public LendService(LendRepository repo) {
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
	}
	
    }
 
}
