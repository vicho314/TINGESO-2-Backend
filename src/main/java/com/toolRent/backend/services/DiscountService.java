package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.DiscountEntity;
import com.toolRent.backend.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class DiscountService{
    @Autowired
    private DiscountRepository discountRepo;
    
    public DiscountService(DiscountRepository discountRepo) {
	//super(discountRepo);
        this.discountRepo = discountRepo;
    }
    
    public DiscountEntity getById(Long id){
	return discountRepo.findById(id).get();
    }

    public List<DiscountEntity> getAll(){
	return (ArrayList<DiscountEntity>) discountRepo.findAll();
    }

    public boolean save(DiscountEntity newFee){
        discountRepo.save(newFee);
	return true;
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
    public boolean update(DiscountEntity newFee){
	discountRepo.save(newFee);
	return true;
    }

    public boolean delete(Long id){
        try {
		discountRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
		//throw new Exception(e.getMessage());
	}
	
    }

    public List<String> getCategories(){
	return discountRepo.findDistinctCategory();
    }
    //FIXME: Check if category exists first!
    public List<DiscountEntity> getDiscountsByCategory(String cat){
	if(this.getCategories().contains(cat)){
		return discountRepo.findAllByCategory(cat);
	}
	else{//FIXME: Optional<>? Exception?
		return null;
	}
    }
}
