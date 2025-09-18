package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.*;
import java.util.List;
import com.toolRent.backend.entities.FeeEntity;

@Entity
@Table(name = "tool")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
    // #FIXME: fix relations and notnull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(unique = true, nullable = false)
    private Long id;
    //private String category;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    // FIXME: Translate this accordingly in frontend!
    private Character state; //(A)vailable, (B)orrowed,(U)nderRepair,(D)own
    //private String rutLastDigit;
    @Column(nullable = false)
    private int replacementValue;
    @OneToOne
    //@JoinTable(
    //	name = "client_transaction",
    //	joinColumns = @JoinColumn(name = "client_id"),
    //	inverseJoinColumns = @JoinColumn(name = "transaction_id")
    //	)
    @JoinColumn(name = "fee_id", referencedColumnName = "id")
    private FeeEntity fee;
    private Integer stock;

    public boolean validState(){
    	boolean result;
    	if(this.state == null){
    		return false;
    	}
    	if(this.state.isWhitespace(this.state)){
    		return false;
    	}
    	// FIXME: this should be checked against DB! Not hardcoded!
    	switch(this.state.toUpperCase(this.state)){
    		case 'A':
    		case 'B':
    		case 'U':
    		case 'D':
    			result = true;
    			break;
    		default:
    			result = false;
    	}
    	return result;
    }

    // FIXME: > or >= ??
    public boolean validReplacement(){
    	return this.replacementValue > 0;
    }

    public boolean validFields(){
    	return this.validState() && this.validReplacement();
    }
}
