package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

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

    @Column(unique = true, nullable = false)
    private String type;
    @Column(nullable = false)
    private Date movementDate;
    @Column(name = "employee_id")
    private Long employeeId;
    @Column(name = "lend_id")
    private Long lendId;
    @Column(name = "tool_id")
    private Long toolId;
}
