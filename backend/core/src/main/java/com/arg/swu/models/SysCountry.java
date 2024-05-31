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
public class SysCountry extends Footprint implements Serializable {

	private static final long serialVersionUID = 7838946600351490376L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long countryId;
	
	private String countryCode;
	private String countryCodeAlpha2;
	private String countryCodeAlpha3;
	private String countryCodeNumeric;
	private String countryNameTh;
	private String countryNameEn;
	
}
