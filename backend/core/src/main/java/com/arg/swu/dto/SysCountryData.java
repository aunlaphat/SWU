package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class SysCountryData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -3220525058649579764L;
	
	private Long countryId;
	private String countryCode;
	private String countryCodeAlpha2;
	private String countryCodeAlpha3;
	private String countryCodeNumeric;
	private String countryNameTh;
	private String countryNameEn;
	
}