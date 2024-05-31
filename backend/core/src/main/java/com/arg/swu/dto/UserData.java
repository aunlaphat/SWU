package com.arg.swu.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class UserData implements Serializable {

	private static final long serialVersionUID = 783809446910117230L;
	
	private Long userId;
	private String firstnameTh;
	private String lastnameTh;
    private String firstnameEn;
	private String lastnameEn;
	private String email;
	private String username;
	private String tel;
	private Boolean isLocal;
	private Boolean activeFlag;
	
	private Long accessLevel;
    private Long depIdLevel1;
    private Long depIdLevel2;
	
}
