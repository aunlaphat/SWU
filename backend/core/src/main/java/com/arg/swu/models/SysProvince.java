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
public class SysProvince extends Footprint implements Serializable {

	private static final long serialVersionUID = -3999921327203955654L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long provinceId;
	
	private String provinceCode;
	private String provinceNameTh;
	private String provinceNameEn;
	private Long regionId;
	
}
