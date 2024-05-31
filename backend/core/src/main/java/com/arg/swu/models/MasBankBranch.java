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
public class MasBankBranch extends Footprint implements Serializable {

	private static final long serialVersionUID = -1891903486667221775L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankBranchId;
	
	private Long bankId;
	private String bankBranchCode;
	private String bankBranchNameTh;
	private String bankBranchNameEn;
	
}
