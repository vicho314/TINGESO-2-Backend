package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.KardexEntity;
import com.toolRent.backend.repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class KardexService{
    @Autowired
    private KardexRepository kardexRepo;

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

    public boolean save(KardexEntity newKardex){
        kardexRepo.save(newKardex);
	return true;
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
    public boolean update(KardexEntity newKardex){
	kardexRepo.save(newKardex);
	return true;
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
 
}
