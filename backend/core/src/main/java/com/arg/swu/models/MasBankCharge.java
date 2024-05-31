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
public class MasBankCharge  extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -340344510844335877L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chargeId;
    
    private Long paymentType;
    private Long cardType;
    private BigDecimal chargeRate;
    private BigDecimal universityChargePercent;
    private BigDecimal studentChargePercent;
    private Long startYear;

}
