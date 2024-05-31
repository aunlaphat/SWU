package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
public class CoursepublicGrade extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -7662788234137848866L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coursepublicGradeId;
	
	private String gradeSymbol;
	private BigDecimal scoreMin;
	private BigDecimal scoreMax;
	private Boolean passStatus;
    private Long coursepublicId;

}
