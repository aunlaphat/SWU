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
public class AutMenu extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 5707679859598169961L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long menuId;
	
	private String menuCode;
	private String menuType;
	private Long parentId;
	private Long orderNumber;
	private String name;
	private String showNameKey;
	private String link;
	
	

}
