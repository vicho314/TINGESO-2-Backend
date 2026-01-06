package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kardex-service", url = "http://kardex-service:8084")
public interface KardexServiceClient {
    
    @PostMapping("/api/v1/kardex/register-movement")
    boolean registerMovement(@RequestBody KardexMovement movement);
    
    class KardexMovement {
        public Long toolId;
        public Long lendId;
        public String movementType; // LEND, RETURN, DAMAGE
        public String description;
        
        public KardexMovement() {}
        
        public KardexMovement(Long toolId, Long lendId, String movementType, String description) {
            this.toolId = toolId;
            this.lendId = lendId;
            this.movementType = movementType;
            this.description = description;
        }
    }
}
