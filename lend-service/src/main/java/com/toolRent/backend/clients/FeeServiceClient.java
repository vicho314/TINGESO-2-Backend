package com.toolRent.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@FeignClient(name = "fee-service", url = "http://fee-service:8083")
public interface FeeServiceClient {
    
    @GetMapping("/api/v1/fee/daily-lending-fee")
    Double getDailyLendingFee();
    
    @GetMapping("/api/v1/fee/daily-late-fee")
    Double getDailyLateFee();
    
    @GetMapping("/api/v1/fee/calculate-late-fee")
    Double calculateLateFee(
        @RequestParam Long toolId,
        @RequestParam Integer daysLate
    );
    
    class FeeInfo {
        public Long id;
        public Double dailyLendingFee;
        public Double dailyLateFee;
        public Long toolId;
    }
}
