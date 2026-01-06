package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tool-service", url = "http://tool-service:8086")
public interface ToolServiceClient {
    
    @GetMapping("/api/v1/tool/{id}")
    ToolInfo getToolById(@PathVariable Long id);
    
    @GetMapping("/api/v1/tool/{id}/state")
    Character getToolState(@PathVariable Long id);
    
    @GetMapping("/api/v1/tool/{id}/stock")
    Integer getToolStock(@PathVariable Long id);
    
    class ToolInfo {
        public Long id;
        public String name;
        public Character state;
        public Integer stock;
        public String category;
    }
}
