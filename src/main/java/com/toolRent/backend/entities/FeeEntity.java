package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.*;
import java.util.List;
import com.toolRent.backend.entities.FeeEntity;

@Entity
@Table(name = "fee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeEntity {
	// #FIXME: fix relations and notnull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    //private String category;
    @Column(unique = true, nullable = false)
    private Integer lendFee ; //#FIXME: float? Int?
    @Column(nullable = false)
    private Integer delayFee;
}
