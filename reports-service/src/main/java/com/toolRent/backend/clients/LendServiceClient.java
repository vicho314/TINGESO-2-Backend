package com.toolRent.backend.clients;

import com.toolRent.backend.dtos.LendingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "lend-service")
public interface LendServiceClient {

    @GetMapping("/api/v1/lend/active")
    List<LendingDTO> getActiveLendings();

    @GetMapping("/api/v1/lend/byDateRange")
    List<LendingDTO> getLendingsByDateRange(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    );

    @GetMapping("/api/v1/lend/late")
    List<LendingDTO> getLateLendings();
}
