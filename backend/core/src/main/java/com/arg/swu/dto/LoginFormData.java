package com.arg.swu.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginFormData implements Serializable {

	private static final long serialVersionUID = -7720180044537578349L;
	
	private String username;
	private String password;
	
	private String token;
	
}
