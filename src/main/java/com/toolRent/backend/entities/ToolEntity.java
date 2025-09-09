package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.*;
import java.util.List;
import com.toolRent.backend.entities.FeeEntity;

@Entity
@Table(name = "tools")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
	// #FIXME: fix relations and notnull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    //private String category;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String ini_state;
    //private String rutLastDigit;
    @Column(nullable = false)
    private int replacementValue;
    @OneToOne
    //@JoinTable(
    //	name = "client_transaction",
    //	joinColumns = @JoinColumn(name = "client_id"),
    //	inverseJoinColumns = @JoinColumn(name = "transaction_id")
    //	)
    private FeeEntity fee;
    private Integer stock;
}
