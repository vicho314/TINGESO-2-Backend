package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.ClientEntity;
import com.toolRent.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class ClientService{
    @Autowired
    private ClientRepository clientRepo;

    public ClientService(ClientRepository repo) {
        //super(repo);
	this.clientRepo = repo;
    }
    
    public ClientEntity getById(Long id){
	return clientRepo.findById(id).get();
    }

    public List<ClientEntity> getAll(){
	return (ArrayList<ClientEntity>) clientRepo.findAll();
    }

    public boolean save(ClientEntity newClient){
        if(newClient.validState()){
		clientRepo.save(newClient);
		return true;
	}
	else{
		return false;
	}
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
    public boolean update(ClientEntity newClient){
	clientRepo.save(newClient);
	return true;
    }

    public boolean delete(Long id){
        try {
		clientRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
		//throw new Exception(e.getMessage());
	}
	
    }
 
}
