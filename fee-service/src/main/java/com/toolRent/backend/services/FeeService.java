package com.toolRent.backend.services;

import com.toolRent.backend.entities.FeeEntity;
import com.toolRent.backend.repositories.FeeRepository;
import com.toolRent.backend.clients.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class FeeService{
    @Autowired
    private FeeRepository feeRepo;
    
    @Autowired
    private UserServiceClient userServiceClient;

    public FeeService(FeeRepository repo) {
	this.feeRepo = repo;
    }
    
    public FeeEntity getById(Long id){
	return feeRepo.findById(id).get();
    }

    public List<FeeEntity> getAll(){
	return (ArrayList<FeeEntity>) feeRepo.findAll();
    }

    public boolean save(FeeEntity newFee){
        feeRepo.save(newFee);
	return true;
    }

    public boolean update(FeeEntity newFee){
	feeRepo.save(newFee);
	return true;
    }

    public boolean delete(Long id){
        try {
		feeRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
	}
	
    }
    
    public Double getDailyLendingFee() {
        try {
            List<FeeEntity> fees = (ArrayList<FeeEntity>) feeRepo.findAll();
            if (fees.isEmpty()) {
                return 0.0;
            }
            // Return the first global fee (toolId = null)
            return fees.stream()
                .filter(fee -> fee.getToolId() == null)
                .findFirst()
                .map(FeeEntity::getDailyLendingFee)
                .orElse(0.0);
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    public Double getDailyLateFee() {
        try {
            List<FeeEntity> fees = (ArrayList<FeeEntity>) feeRepo.findAll();
            if (fees.isEmpty()) {
                return 0.0;
            }
            // Return the first global fee (toolId = null)
            return fees.stream()
                .filter(fee -> fee.getToolId() == null)
                .findFirst()
                .map(FeeEntity::getDailyLateFee)
                .orElse(0.0);
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    public Double calculateLateFee(Long toolId, Integer daysLate) {
        try {
            Optional<FeeEntity> feeOpt = feeRepo.findAll().stream()
                .filter(fee -> fee.getToolId() != null && fee.getToolId().equals(toolId))
                .findFirst();
            
            Double dailyLateFee;
            if (feeOpt.isPresent()) {
                dailyLateFee = feeOpt.get().getDailyLateFee();
            } else {
                dailyLateFee = getDailyLateFee();
            }
            
            return dailyLateFee * daysLate;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    public boolean setDailyLendingFee(Long userId, Double amount) {
        try {
            // Validate user is admin
            if (!userServiceClient.validatePermission(userId, "ADMIN")) {
                return false;
            }
            
            List<FeeEntity> fees = (ArrayList<FeeEntity>) feeRepo.findAll();
            Optional<FeeEntity> globalFeeOpt = fees.stream()
                .filter(fee -> fee.getToolId() == null)
                .findFirst();
            
            FeeEntity fee;
            if (globalFeeOpt.isPresent()) {
                fee = globalFeeOpt.get();
            } else {
                fee = new FeeEntity();
                fee.setReplacementValue(0); // Default value
                fee.setDailyLateFee(0.0);
            }
            
            fee.setDailyLendingFee(amount);
            feeRepo.save(fee);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean setDailyLateFee(Long userId, Double amount) {
        try {
            // Validate user is admin
            if (!userServiceClient.validatePermission(userId, "ADMIN")) {
                return false;
            }
            
            List<FeeEntity> fees = (ArrayList<FeeEntity>) feeRepo.findAll();
            Optional<FeeEntity> globalFeeOpt = fees.stream()
                .filter(fee -> fee.getToolId() == null)
                .findFirst();
            
            FeeEntity fee;
            if (globalFeeOpt.isPresent()) {
                fee = globalFeeOpt.get();
            } else {
                fee = new FeeEntity();
                fee.setReplacementValue(0);
                fee.setDailyLendingFee(0.0);
            }
            
            fee.setDailyLateFee(amount);
            feeRepo.save(fee);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean setReplacementValue(Long userId, Long toolId, Integer value) {
        try {
            // Validate user is admin
            if (!userServiceClient.validatePermission(userId, "ADMIN")) {
                return false;
            }
            
            Optional<FeeEntity> feeOpt = feeRepo.findAll().stream()
                .filter(fee -> fee.getToolId() != null && fee.getToolId().equals(toolId))
                .findFirst();
            
            FeeEntity fee;
            if (feeOpt.isPresent()) {
                fee = feeOpt.get();
            } else {
                fee = new FeeEntity();
                fee.setToolId(toolId);
                fee.setDailyLendingFee(0.0);
                fee.setDailyLateFee(0.0);
            }
            
            fee.setReplacementValue(value);
            feeRepo.save(fee);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Integer getReplacementValue(Long toolId) {
        try {
            Optional<FeeEntity> feeOpt = feeRepo.findAll().stream()
                .filter(fee -> fee.getToolId() != null && fee.getToolId().equals(toolId))
                .findFirst();
            
            return feeOpt.map(FeeEntity::getReplacementValue).orElse(0);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public List<FeeEntity> getToolFees(Long toolId) {
        try {
            return (ArrayList<FeeEntity>) feeRepo.findAll().stream()
                .filter(fee -> fee.getToolId() != null && fee.getToolId().equals(toolId))
                .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
 
}
