package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysPersonalProgress extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -2031641718727812741L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long row;
	private BigDecimal progress;
	private Long toTemp;
	private Long toActual;

}
