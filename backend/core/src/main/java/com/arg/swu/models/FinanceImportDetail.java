package com.arg.swu.models;

import com.arg.swu.models.commons.Footprint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author sutthiyapakc
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FinanceImportDetail extends Footprint implements Serializable {

	private static final long serialVersionUID = 1272673560702186452L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long detailId;
    private Long impId;
    private Long coursepublicId;
	private Date payDatetime;	
	private BigDecimal payAmount;
	private String memberIdCard;
	private Long prefixnameId;
	private String prefix;
	private String firstnameTh;
	private String lastnameTh;
	private String firstnameEn;
	private String lastnameEn;
	private String email;
	private String address;
	private String orgLegalCode;
	private String orgName;
	private String orgAddress;
	
	private String detailStatus;
	private String errorMessage;

	private Boolean gentPdfFlag;
    
}
