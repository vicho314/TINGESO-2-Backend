package com.toolRent.backend.services;

//import com.toolRent.backend.services.AbstractService;
import com.toolRent.backend.entities.TransactionEntity;
import com.toolRent.backend.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;
// get, save, update, delete
@Service
public class TransactionService{
    @Autowired
    private TransactionRepository transactionRepo;

    public TransactionService(TransactionRepository repo) {
        //super(repo);
	this.transactionRepo = repo;
    }
    
    public TransactionEntity getById(Long id){
	return transactionRepo.findById(id).get();
    }

    public List<TransactionEntity> getAll(){
	return (ArrayList<TransactionEntity>) transactionRepo.findAll();
    }

    public boolean save(TransactionEntity newFee){
        transactionRepo.save(newFee);
	return true;
    }

    //FIXME: assume it already exists?
    // Should the repo, service or controller do the check?
    public boolean update(TransactionEntity newFee){
	transactionRepo.save(newFee);
	return true;
    }

    public boolean delete(Long id){
        try {
		transactionRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
		//throw new Exception(e.getMessage());
	}
	
    }

    public TransactionEntity getTransactionByIdAndDate(Long id, LocalDateTime t){
	    return transactionRepo.findByIdAndReservationDate(id, t);
    }
}
