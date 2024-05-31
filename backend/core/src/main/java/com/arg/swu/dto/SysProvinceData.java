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
public class SysProvinceData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 1548414897510382207L;
	
	private Long provinceId;
	private String provinceCode;
	private String provinceNameTh;
	private String provinceNameEn;
	private Long regionId;
	
}