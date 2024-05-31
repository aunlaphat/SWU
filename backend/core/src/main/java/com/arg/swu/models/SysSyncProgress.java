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
public class SysSyncProgress extends Footprint implements Serializable {

	private static final long serialVersionUID = 2508147817098071109L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String tableName;
	private Long executeRow;
	private BigDecimal progress;
	private Long toTemp;
	private Long toActual;
	
}
