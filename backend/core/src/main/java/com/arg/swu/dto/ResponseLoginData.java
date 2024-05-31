package com.arg.swu.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@AllArgsConstructor
public class ResponseLoginData implements Serializable {

	private static final long serialVersionUID = 1638358828730728040L;
	
	private UserData user;
	private List<AllMenuData> allMenu;
	private String token;
	
}
