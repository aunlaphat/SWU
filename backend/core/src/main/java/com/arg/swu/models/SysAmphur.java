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
public class SysAmphur extends Footprint implements Serializable {

	private static final long serialVersionUID = -7906526523694187836L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long amphurId;
	
	private Long provinceId;
	
	private String provinceCode;
	private String amphurCode;
	private String amphurNameTh;
	private String amphurNameEn;
	
}
