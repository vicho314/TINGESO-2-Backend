package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.KardexEntity;
import com.toolRent.backend.repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.toolRent.backend.services.ToolService;
import com.toolRent.backend.entities.ToolEntity;
import com.toolRent.backend.services.LendService;
import com.toolRent.backend.entities.LendEntity;


import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class KardexService{
    @Autowired
    private KardexRepository kardexRepo;
    @Autowired
    private ToolService toolService;
    @Autowired
    private LendService lendService;

    public KardexService(KardexRepository repo) {
        //super(repo);
	this.kardexRepo = repo;
    }
    
    public KardexEntity getById(Long id){
	return kardexRepo.findById(id).get();
    }

    public List<KardexEntity> getAll(){
	return (ArrayList<KardexEntity>) kardexRepo.findAll();
    }

    public KardexEntity save(KardexEntity newKardex){
        KardexEntity kardex = kardexRepo.save(newKardex);
	return kardex;
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
    public KardexEntity update(KardexEntity newKardex){
	return kardexRepo.save(newKardex);
    }

    public boolean delete(Long id){
        try {
		kardexRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
		//throw new Exception(e.getMessage());
	}
	
    }
    
    public KardexEntity saveTool(KardexEntity newKardex){
        ToolEntity newTool = newKardex.getTool();
	newKardex.setTool(toolService.save(newTool));
	newKardex.setType("I"); //Ingress
	return this.save(newKardex);
    }
    
    public KardexEntity takeDownTool(KardexEntity newKardex){
        ToolEntity tool = newKardex.getTool();
	if(tool.getStock() <= 1){
		tool.setStock(0);
		tool.setState('D');
	}	
	else{
		tool.setStock(tool.getStock()-1);
	}
	newKardex.setTool(toolService.update(tool));
	newKardex.setType("T"); //TakeDown
	return this.save(newKardex);
    }
    
    public KardexEntity repairTool(KardexEntity newKardex){
        ToolEntity tool = newKardex.getTool();
	if(tool.getStock() <= 1){
		tool.setStock(0);
		tool.setState('R');
	}
	else{
		tool.setStock(tool.getStock()-1);
	}
	newKardex.setTool(toolService.update(tool));
	newKardex.setType("R"); //Repair
	return this.save(newKardex);
    }

    public KardexEntity saveLend(KardexEntity newKardex){
	LendEntity newLend = newKardex.getLend();
	newKardex.setLend(lendService.save(newLend));
	newKardex.setType("L"); //Lend
	return this.save(newKardex);
    }
}
