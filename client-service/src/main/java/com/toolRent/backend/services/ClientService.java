package com.toolRent.backend.services;

import com.toolRent.backend.entities.ClientEntity;
import com.toolRent.backend.repositories.ClientRepository;
import com.toolRent.backend.clients.LendServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClientService{
    @Autowired
    private ClientRepository clientRepo;
    
    @Autowired
    private LendServiceClient lendServiceClient;

    public ClientService(ClientRepository repo) {
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
	}
	
    }
    
    public boolean checkAndRestrictClient(Long clientId) {
        try {
            Optional<ClientEntity> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                return false;
            }
            
            ClientEntity client = clientOpt.get();
            
            // Get client lendings
            List<LendServiceClient.LendInfo> lendings = lendServiceClient.getClientLendings(clientId);
            
            // Check for active lendings with delayed return date
            LocalDate today = LocalDate.now();
            boolean hasDelays = false;
            long totalDaysLate = 0;
            
            for (LendServiceClient.LendInfo lend : lendings) {
                // Only check active lendings
                if ("A".equals(lend.state) && lend.returnDay != null) {
                    if (today.isAfter(lend.returnDay)) {
                        hasDelays = true;
                        long daysLate = ChronoUnit.DAYS.between(lend.returnDay, today);
                        totalDaysLate += daysLate;
                    }
                }
            }
            
            // If delays exist, restrict the client
            if (hasDelays) {
                client.setClientState('R'); // 'R' for Restricted
                client.setRestrictedSince(LocalDateTime.now());
                client.setRestrictionReason("Delayed return of tools (" + totalDaysLate + " days overdue)");
                clientRepo.save(client);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean unrestrictClient(Long clientId) {
        try {
            Optional<ClientEntity> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                return false;
            }
            
            ClientEntity client = clientOpt.get();
            
            // Check if all lendings are returned
            List<LendServiceClient.LendInfo> lendings = lendServiceClient.getClientLendings(clientId);
            boolean hasActiveLendings = lendings.stream()
                .anyMatch(lend -> "A".equals(lend.state));
            
            if (!hasActiveLendings) {
                client.setClientState('A'); // 'A' for Active
                client.setRestrictedSince(null);
                client.setRestrictionReason(null);
                clientRepo.save(client);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getClientState(Long clientId) {
        try {
            Optional<ClientEntity> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                return null;
            }
            
            ClientEntity client = clientOpt.get();
            return client.getClientState() == 'A' ? "Active" : "Restricted";
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ClientEntity> getRestrictedClients() {
        return (ArrayList<ClientEntity>) clientRepo.findAll()
            .stream()
            .filter(client -> client.getClientState() == 'R')
            .toList();
    }
 
}
