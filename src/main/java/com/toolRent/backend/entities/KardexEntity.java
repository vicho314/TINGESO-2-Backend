package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.*;
import java.util.List;
import com.toolRent.backend.entities.UserEntity;
import com.toolRent.backend.entities.ToolEntity;

@Entity
@Table(name = "kardex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KardexEntity {
	// #FIXME: fix relations and notnull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    //private String category;
    @Column(unique = true, nullable = false)
    private String type; //#FIXME: specify types
    @Column(nullable = false)
    private Date movementDate;
    @Column(nullable = false)
    @OneToOne
    private UserEntity user;
    //private String rutLastDigit;
    @Column(nullable = false)
    @OneToOne
    private ToolEntity tool;

}
