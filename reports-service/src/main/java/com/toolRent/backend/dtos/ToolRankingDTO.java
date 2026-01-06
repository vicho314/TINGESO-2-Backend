package com.toolRent.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolRankingDTO {
    private Long toolId;
    private String toolName;
    private String toolCategory;
    private Long totalLendings;
    private Long rank;
}
