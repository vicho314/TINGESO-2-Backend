package com.toolRent.backend.services;

import com.toolRent.backend.entities.ToolEntity;
import com.toolRent.backend.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class ToolService{
    @Autowired
    private ToolRepository toolRepo;

    public ToolService(ToolRepository repo) {
	this.toolRepo = repo;
    }
    
    public ToolEntity getById(Long id){
	return toolRepo.findById(id).get();
    }

    public List<ToolEntity> getAll(){
	return (ArrayList<ToolEntity>) toolRepo.findAll();
    }

    public boolean save(ToolEntity newTool){
        if(newTool.validFields()){
		toolRepo.save(newTool);
		return true;
	}
	else{
		return false;
	}
    }

    public boolean update(ToolEntity newTool){
	toolRepo.save(newTool);
	return true;
    }

    public boolean delete(Long id){
        try {
		toolRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
	}
	
    }
 
}
