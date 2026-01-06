package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "tool-service")
public interface ToolServiceClient {

    @GetMapping("/api/v1/tool/{id}")
    ToolInfo getToolById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/tool/all")
    List<ToolInfo> getAllTools();

    class ToolInfo {
        public Long id;
        public String name;
        public String category;
        public String state;
        public Integer stock;
    }
}
