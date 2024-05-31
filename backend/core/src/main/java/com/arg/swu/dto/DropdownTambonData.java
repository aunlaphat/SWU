package com.arg.swu.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class DropdownTambonData extends DropdownData implements Serializable {

	private static final long serialVersionUID = -6388696827093410904L;
	
	private Object zipCode;
	
}
