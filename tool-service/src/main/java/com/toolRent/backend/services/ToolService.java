package com.toolRent.backend.services;

import com.toolRent.backend.entities.ToolEntity;
import com.toolRent.backend.repositories.ToolRepository;
import com.toolRent.backend.clients.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ToolService{
    @Autowired
    private ToolRepository toolRepo;
    
    @Autowired
    private UserServiceClient userServiceClient;

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
    public boolean takedownTool(Long toolId, Long userId, String reason) {
        try {
            // Validate user has admin role
            if (!userServiceClient.validatePermission(userId, "ADMIN")) {
                return false;
            }
            
            Optional<ToolEntity> toolOpt = toolRepo.findById(toolId);
            if (toolOpt.isEmpty()) {
                return false;
            }
            
            ToolEntity tool = toolOpt.get();
            tool.setState('D'); // 'D' for Down/Taken down
            tool.setTakedownDate(LocalDateTime.now());
            tool.setTakedownReason(reason);
            
            toolRepo.save(tool);
            return true;
        } catch (Exception e) {
            return false;
        }
    } 
}
