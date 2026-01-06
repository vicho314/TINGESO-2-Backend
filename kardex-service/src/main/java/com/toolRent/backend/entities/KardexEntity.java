package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kardex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KardexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String movementType; // LEND, RETURN, DAMAGE, REPAIR, MAINTENANCE
    @Column(nullable = false)
    private LocalDateTime movementDate;
    @Column(name = "lend_id")
    private Long lendId;
    @Column(name = "tool_id", nullable = false)
    private Long toolId;
    private String description;
    @Column(name = "created_by")
    private Long createdBy;
}
