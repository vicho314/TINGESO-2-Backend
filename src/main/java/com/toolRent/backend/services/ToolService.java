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

    // FIXME: Null!! Use proper handler! -> Optional<>
    public ToolEntity save(ToolEntity newTool){
        if(newTool.validFields())
		return toolRepo.save(newTool);
	else
		return null;
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
    // FIXME: Check Admin priv. in controller!!
    public boolean takeDown(ToolEntity newTool){
	newTool.setState('D'); // void is... pain.
    	return this.update(newTool);
    }
}
