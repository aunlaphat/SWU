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
public class MasBankAccount extends Footprint implements Serializable {

	private static final long serialVersionUID = 9099014538462967213L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankAccountId;
	
	private Long bankId;
	private Long bankBranchId;
	private String accountNo;
	private String accountNameTh;
	private String accountNameEn;
	private String billerId;
	private String companyId;
	private String bankAccountNote;
}
