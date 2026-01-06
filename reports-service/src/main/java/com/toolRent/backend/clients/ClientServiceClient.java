package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service")
public interface ClientServiceClient {

    @GetMapping("/api/v1/client/{id}")
    ClientInfo getClientById(@PathVariable("id") Long id);

    class ClientInfo {
        public Long id;
        public String name;
        public String rut;
        public String mail;
        public String state; // "Active" or "Restricted"
    }
}
