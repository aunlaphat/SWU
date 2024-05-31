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
public class MasOccupationSkill extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -7028022481933501274L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long occSkillId;
	
	private Long occupationId;
	private Long generalSkillId;
	private Long occSkillLevel;

}
