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
public class AutPermission extends Footprint implements Serializable {

	private static final long serialVersionUID = 2036041972547535919L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long permissionId;
	
	private Long roleId;
	private Long menuId;
	
}
