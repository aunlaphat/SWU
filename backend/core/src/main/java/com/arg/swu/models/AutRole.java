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
public class AutRole extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 4679997528661759881L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;
	
	private String name;
	
}
