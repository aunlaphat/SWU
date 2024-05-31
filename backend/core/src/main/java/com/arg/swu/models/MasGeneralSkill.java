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
public class MasGeneralSkill extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -8816624191285503797L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long generalSkillId;
	
	private String generalSkillCode;
	private String generalSkillNameTh;
	private String generalSkillNameEn;
	private Long skillFormat;
	private String filename;
	private String skillImage;
	private String prefix;
	private Long module;

}
