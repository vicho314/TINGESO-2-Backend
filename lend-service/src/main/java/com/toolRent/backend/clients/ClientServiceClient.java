package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service", url = "http://client-service:8081")
public interface ClientServiceClient {
    
    @GetMapping("/api/v1/client/{id}")
    ClientInfo getClientById(@PathVariable Long id);
    
    @GetMapping("/api/v1/client/{id}/state")
    String getClientState(@PathVariable Long id);
    
    class ClientInfo {
        public Long id;
        public String name;
        public String state;
        public String dni;
    }
}
