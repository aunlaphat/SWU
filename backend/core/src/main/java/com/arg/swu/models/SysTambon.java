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
public class SysTambon extends Footprint implements Serializable {

	private static final long serialVersionUID = -2353524723441319755L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tambonId;
	
	private Long amphurId;
	private String amphurCode;
	private String tambonCode;
	private String tambonNameTh;
	private String tambonNameEn;
	private String zipCode;
	
}