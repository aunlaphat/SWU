package com.arg.swu.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class DropdownData implements Serializable {

	private static final long serialVersionUID = -3271714466279391674L;
	
	private Object value;
	private String nameTh;
	private String nameEn;
	
}
