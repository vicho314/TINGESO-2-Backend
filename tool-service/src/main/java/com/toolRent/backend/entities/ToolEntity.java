package com.toolRent.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tool")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private Character state;
    @Column(nullable = false)
    private int replacementValue;
    @Column(name = "fee_id")
    private Long feeId;
    private Integer stock;
    private LocalDateTime takedownDate;
    private String takedownReason;

    public boolean validState(){
    	boolean result;
    	if(this.state == null){
    		return false;
    	}
    	if(this.state.isWhitespace(this.state)){
    		return false;
    	}
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

    public boolean validReplacement(){
    	return this.replacementValue > 0;
    }

    public boolean validFields(){
    	return this.validState() && this.validReplacement();
    }
}
