package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "lend-service", url = "http://lend-service:8085")
public interface LendServiceClient {
    
    @GetMapping("/api/v1/lend/client/{clientId}/lendings")
    List<LendInfo> getClientLendings(@PathVariable Long clientId);
    
    @GetMapping("/api/v1/lend/client/{clientId}/is-blocked")
    boolean isClientBlocked(@PathVariable Long clientId);
    
    class LendInfo {
        public Long id;
        public Long clientId;
        public Long toolId;
        public String state; // A for Active, R for Returned
        public java.time.LocalDate deliveryDay;
        public java.time.LocalDate returnDay;
        public Boolean isBlocked;
    }
}
