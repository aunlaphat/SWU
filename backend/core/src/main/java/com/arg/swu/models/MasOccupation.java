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
public class MasOccupation  extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 8553617802383568622L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long occupationId;

    private Long occupationGroupId;
    private String occupationCode;
    private String occupationNameTh;
    private String occupationNameEn;

}
