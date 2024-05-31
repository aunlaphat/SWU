package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

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
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysProgressData implements Serializable {

	private static final long serialVersionUID = 8227337503596134287L;

	private Long id;
	private String tableName;
	private Long executeRow;
	private BigDecimal progress;
	private Long toTemp;
	private Long toActual;
	private Boolean activeFlag;
	
}
