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
public class SysAmphurData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 2694209877152817205L;
	
	private Long amphurId;
	private Long provinceId;
	private String provinceCode;
	private String amphurCode;
	private String amphurNameTh;
	private String amphurNameEn;
	
}