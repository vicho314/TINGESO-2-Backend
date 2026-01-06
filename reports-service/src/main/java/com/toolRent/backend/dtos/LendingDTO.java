package com.toolRent.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingDTO {
    private Long id;
    private Long clientId;
    private Long toolId;
    private String clientName;
    private String toolName;
    private LocalDate deliveryDay;
    private LocalDate returnDay;
    private String state; // "ACTIVE", "LATE", "RETURNED"
    private Long daysLate;
}
