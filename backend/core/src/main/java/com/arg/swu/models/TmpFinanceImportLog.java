package com.arg.swu.models;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author sitthichaim
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TmpFinanceImportLog extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -6150765310296652261L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tmpImpId;
	
	private String uuid;
	private String fileReferenceCode;
	private String impFileName;
	private String impFileLink;
	private Long impFileSize;
	private Long impFileRow;
	private Long passRow;
	private Long failRow;
	private Long missRow;
	private String messageError;
	private BigDecimal impFileMoney;
	private Long coursepublicId;
	private String prefix;
	private Long module;
    
}
