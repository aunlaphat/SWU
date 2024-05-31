package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
public class SwuCurriculum extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 8504267080784130677L;
	
	@Id
	private String curriculumSwuId;
	
	private String curriculumMhesiId;
	private String curriculumNameTh;
	private String curriculumNameEn;
	private BigDecimal creditStudy;
	private String degreeCode;
	private String degreeShortTh;
	private String degreeNameTh;
	private String degreeShortEn;
	private String degreeNameEn;
	private String majorCode;
	private String majorNameTh;
	private String majorNameEn;
	private String eduLevelCode;
	private String eduLevelNameTh;
	private String eduLevelNameEn;
	private BigDecimal studyYear;
	private String depCode;

	private Long chairmanId;
	private String ownerDepCode;
	private String ownerDepNameTh;
	private String ownerDepNameEn;
	private String ownerFacultyCode;
	private String ownerFacultyNameTh;
	private String ownerFacultyNameEn;


	
}
