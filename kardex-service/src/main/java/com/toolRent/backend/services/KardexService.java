package com.toolRent.backend.services;

import com.toolRent.backend.entities.KardexEntity;
import com.toolRent.backend.repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class KardexService{
    @Autowired
    private KardexRepository kardexRepo;

    public KardexService(KardexRepository repo) {
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
	}
	
    }
    
    public boolean registerMovement(Long toolId, Long lendId, String movementType, String description, Long createdBy) {
        try {
            KardexEntity movement = new KardexEntity();
            movement.setToolId(toolId);
            movement.setLendId(lendId);
            movement.setMovementType(movementType); // LEND, RETURN, DAMAGE, REPAIR, MAINTENANCE
            movement.setDescription(description);
            movement.setCreatedBy(createdBy);
            movement.setMovementDate(LocalDateTime.now());
            
            kardexRepo.save(movement);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean registerMovement(KardexEntity movement) {
        try {
            movement.setMovementDate(LocalDateTime.now());
            kardexRepo.save(movement);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<KardexEntity> getToolHistory(Long toolId) {
        try {
            return (ArrayList<KardexEntity>) kardexRepo.findAll()
                .stream()
                .filter(kardex -> kardex.getToolId().equals(toolId))
                .sorted((a, b) -> b.getMovementDate().compareTo(a.getMovementDate()))
                .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<KardexEntity> getLendingHistory(Long lendId) {
        try {
            return (ArrayList<KardexEntity>) kardexRepo.findAll()
                .stream()
                .filter(kardex -> kardex.getLendId() != null && kardex.getLendId().equals(lendId))
                .sorted((a, b) -> b.getMovementDate().compareTo(a.getMovementDate()))
                .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<KardexEntity> getMovementsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
            
            return (ArrayList<KardexEntity>) kardexRepo.findAll()
                .stream()
                .filter(kardex -> {
                    LocalDateTime movDate = kardex.getMovementDate();
                    return !movDate.isBefore(startDateTime) && movDate.isBefore(endDateTime);
                })
                .sorted((a, b) -> b.getMovementDate().compareTo(a.getMovementDate()))
                .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<KardexEntity> getToolMovementsByDateRange(Long toolId, LocalDate startDate, LocalDate endDate) {
        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
            
            return (ArrayList<KardexEntity>) kardexRepo.findAll()
                .stream()
                .filter(kardex -> {
                    LocalDateTime movDate = kardex.getMovementDate();
                    return kardex.getToolId().equals(toolId) &&
                           !movDate.isBefore(startDateTime) && 
                           movDate.isBefore(endDateTime);
                })
                .sorted((a, b) -> b.getMovementDate().compareTo(a.getMovementDate()))
                .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<KardexEntity> getMovementsByType(String movementType) {
        try {
            return (ArrayList<KardexEntity>) kardexRepo.findAll()
                .stream()
                .filter(kardex -> kardex.getMovementType().equalsIgnoreCase(movementType))
                .sorted((a, b) -> b.getMovementDate().compareTo(a.getMovementDate()))
                .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<KardexEntity> getRecentMovements(int limit) {
        try {
            return (ArrayList<KardexEntity>) kardexRepo.findAll()
                .stream()
                .sorted((a, b) -> b.getMovementDate().compareTo(a.getMovementDate()))
                .limit(limit)
                .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
 
}
