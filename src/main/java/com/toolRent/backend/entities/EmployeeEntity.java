package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.*;
import java.util.List;
//import com.toolRent.backend.entities.FeeEntity;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
	// #FIXME: fix relations and notnull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    //private String category;
    @Column(unique = true, nullable = false)
    private String name;
    private String mail; //#FIXME: notnull!
    private String rut;
    @Column(nullable = false)
    private String role; // #FIXME: hardcoded or from DB?
    @Column(nullable = false)
    private String password; //hash! #FIXME: JSON Web token (?)
}
