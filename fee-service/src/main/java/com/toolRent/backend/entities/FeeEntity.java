package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "fee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Double dailyLendingFee; // Daily fee for active lending
    @Column(nullable = false)
    private Double dailyLateFee; // Daily fee for overdue lending
    @Column(name = "tool_id")
    private Long toolId; // Tool-specific fee configuration
    @Column(nullable = false)
    private Integer replacementValue; // Replacement cost (admin only)
}
