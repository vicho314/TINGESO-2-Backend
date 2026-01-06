package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lend")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Character state;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "tool_id")
    private Long toolId;
    private LocalDate deliveryDay;
    @Column(nullable = false)
    private LocalDate returnDay;
    private Long lateFeeId;
    private Boolean isBlocked;
}
