package com.arg.swu.models;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AutUserRolePK implements Serializable {
	
	private static final long serialVersionUID = 5819678704034709889L;

	private Long userId;
	private Long roleId;
	
}
