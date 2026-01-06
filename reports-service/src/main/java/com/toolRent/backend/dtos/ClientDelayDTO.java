package com.toolRent.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDelayDTO {
    private Long clientId;
    private String clientName;
    private String clientDNI;
    private Integer delayedLendings;
    private Long totalDaysLate;
    private String clientState; // "Active" or "Restricted"
}
