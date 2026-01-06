package com.toolRent.backend.services;

import com.toolRent.backend.entities.LendEntity;
import com.toolRent.backend.repositories.LendRepository;
import com.toolRent.backend.clients.ClientServiceClient;
import com.toolRent.backend.clients.ToolServiceClient;
import com.toolRent.backend.clients.FeeServiceClient;
import com.toolRent.backend.clients.KardexServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class LendService{
    @Autowired
    private LendRepository lendRepo;
    
    @Autowired
    private ClientServiceClient clientServiceClient;
    
    @Autowired
    private ToolServiceClient toolServiceClient;
    
    @Autowired
    private FeeServiceClient feeServiceClient;
    
    @Autowired
    private KardexServiceClient kardexServiceClient;

    public LendService(LendRepository repo) {
	this.lendRepo = repo;
    }
    
    public LendEntity getById(Long id){
	return lendRepo.findById(id).get();
    }

    public List<LendEntity> getAll(){
	return (ArrayList<LendEntity>) lendRepo.findAll();
    }

    public boolean save(LendEntity newLend){
        lendRepo.save(newLend);
	return true;
    }

    public boolean update(LendEntity newLend){
	lendRepo.save(newLend);
	return true;
    }

    public boolean delete(Long id){
        try {
		lendRepo.deleteById(id);
		return true;
	
	} catch (Exception e) {
		return false;
	}
	
    }
    
    public boolean createLending(LendEntity lend) {
        try {
            // Validate client exists and is not Restricted
            String clientState = clientServiceClient.getClientState(lend.getClientId());
            if ("Restricted".equals(clientState)) {
                return false; // Client blocked due to delays
            }
            
            // Validate tool is available with sufficient stock
            Character toolState = toolServiceClient.getToolState(lend.getToolId());
            if (toolState != 'A') { // 'A' for Available
                return false;
            }
            
            Integer toolStock = toolServiceClient.getToolStock(lend.getToolId());
            if (toolStock == null || toolStock <= 0) {
                return false;
            }
            
            // Set state to active
            lend.setState('A'); // 'A' for Active
            lend.setDeliveryDay(LocalDate.now());
            lend.setIsBlocked(false);
            
            // Save the lending
            LendEntity savedLend = lendRepo.save(lend);
            
            // Register kardex movement
            KardexServiceClient.KardexMovement movement = 
                new KardexServiceClient.KardexMovement(
                    lend.getToolId(),
                    savedLend.getId(),
                    "LEND",
                    "Tool lent to client " + lend.getClientId()
                );
            kardexServiceClient.registerMovement(movement);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Double calculateLateFees(Long lendId) {
        try {
            Optional<LendEntity> lendOpt = lendRepo.findById(lendId);
            if (lendOpt.isEmpty()) {
                return 0.0;
            }
            
            LendEntity lend = lendOpt.get();
            LocalDate today = LocalDate.now();
            
            // Check if lending is overdue
            if (!today.isAfter(lend.getReturnDay())) {
                return 0.0; // Not overdue
            }
            
            // Calculate days late
            long daysLate = ChronoUnit.DAYS.between(lend.getReturnDay(), today);
            
            // Get late fee from fee service
            Double lateFee = feeServiceClient.calculateLateFee(lend.getToolId(), (int) daysLate);
            
            return lateFee;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    public boolean isClientBlocked(Long clientId) {
        try {
            String clientState = clientServiceClient.getClientState(clientId);
            return "Restricted".equals(clientState);
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<LendEntity> getClientLendings(Long clientId) {
        return (ArrayList<LendEntity>) lendRepo.findAll()
            .stream()
            .filter(lend -> lend.getClientId().equals(clientId))
            .toList();
    }
    
    public boolean returnLending(Long lendId) {
        try {
            Optional<LendEntity> lendOpt = lendRepo.findById(lendId);
            if (lendOpt.isEmpty()) {
                return false;
            }
            
            LendEntity lend = lendOpt.get();
            lend.setState('R'); // 'R' for Returned
            lendRepo.save(lend);
            
            // Register kardex return movement
            KardexServiceClient.KardexMovement returnMovement = 
                new KardexServiceClient.KardexMovement(
                    lend.getToolId(),
                    lendId,
                    "RETURN",
                    "Tool returned by client " + lend.getClientId()
                );
            kardexServiceClient.registerMovement(returnMovement);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
 
}
