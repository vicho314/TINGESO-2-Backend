package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.ToolEntity;
import com.toolRent.backend.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class ToolService{
    @Autowired
    private ToolRepository toolRepo;

    public ToolService(ToolRepository repo) {
        //super(repo);
	this.toolRepo = repo;
    }
    
    public ToolEntity getById(Long id){
	return toolRepo.findById(id).get();
    }

    public List<ToolEntity> getAll(){
	return (ArrayList<ToolEntity>) toolRepo.findAll();
    }

    public boolean save(ToolEntity newTool){
        toolRepo.save(newTool);
	return true;
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
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
		//throw new Exception(e.getMessage());
	}
	
    }
 
}
