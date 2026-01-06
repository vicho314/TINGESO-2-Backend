package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://user-service:8088")
public interface UserServiceClient {
    
    @GetMapping("/api/v1/user/{id}/validate-permission")
    boolean validatePermission(
        @PathVariable Long id,
        @RequestParam String requiredRole
    );
}
