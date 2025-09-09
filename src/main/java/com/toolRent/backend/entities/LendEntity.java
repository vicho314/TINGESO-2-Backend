package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.*;
import java.util.List;
import com.toolRent.backend.entities.ClientEntity;
import com.toolRent.backend.entities.ToolEntity;

@Entity
@Table(name = "lends")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendEntity {
	// #FIXME: fix relations and notnull
	// #FIXME: relation or entity? PK or PKs?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    //private String category;
    //@Column(unique = true, nullable = false)
    @OneToOne
    private ClientEntity client;
    //@Column(nullable = false)
    @OneToOne
    private ToolEntity fee;
    //@Column(nullable = false)
    private Date deliveryDay; //#FIXME: Date type
    @Column(nullable = false)
    private Date returnDay;
    //@JoinTable(
    //	name = "client_transaction",
    //	joinColumns = @JoinColumn(name = "client_id"),
    //	inverseJoinColumns = @JoinColumn(name = "transaction_id")
    //	)
}
