package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MasGeneralSkillLevel extends Footprint implements Serializable{

	private static final long serialVersionUID = 1532191011580565292L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long skillLevelId;
	
	private Long generalSkillId;
	private Long levelNo;
	private String descTh;
	private String descEn;
	private String evaluationEvident;
	private String evaluationCriteria;
}
