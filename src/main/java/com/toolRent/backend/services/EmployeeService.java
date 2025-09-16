package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.UserEntity;
import com.toolRent.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class UserService{
    @Autowired
    private UserRepository userRepo;

    public UserService(UserRepository repo) {
        //super(repo);
	this.userRepo = repo;
    }
    
    public UserEntity getById(Long id){
	return userRepo.findById(id).get();
    }

    public List<UserEntity> getAll(){
	return (ArrayList<UserEntity>) userRepo.findAll();
    }

    public boolean save(UserEntity newUser){
        userRepo.save(newUser);
	return true;
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
    public boolean update(UserEntity newUser){
	userRepo.save(newUser);
	return true;
    }

    public boolean delete(Long id){
        try {
		userRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
		//throw new Exception(e.getMessage());
	}
	
    }
 
}
