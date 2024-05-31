package com.arg.swu.models;

import java.io.Serializable;

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
public class MasBankChargeAttach  extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 2804605087162402988L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chargeAttachId;
    
    private Long chargeId;
    private String fileName;
    private String fileLink;
	
}
