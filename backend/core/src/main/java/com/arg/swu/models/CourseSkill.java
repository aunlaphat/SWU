package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CourseSkill extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 4669277653266979930L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseSkillId;
	
	private Long courseId;
	private Long generalSkillId;
	private Boolean courseSkillOtherStatus;
	private String courseSkillOtherNameTh;
	private String courseSkillOtherNameEn;
	private Long skillLevel;
	private BigDecimal skillWeight;

}
