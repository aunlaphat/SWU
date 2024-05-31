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
public class SysPrefixnameData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 8176140795660187068L;
	
	private Long prefixnameId;
	private String prefixnameCode;
	private String prefixnameNameTh;
	private String prefixnameNameEn;
	private String prefixnameShortTh;
	private String prefixnameShortEn;
	
}