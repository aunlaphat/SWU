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
public class SwuSubject extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 7442773756063135258L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subjectSwuId;
	
	private String curriculumSwuId;
	private String curriculumMhesiId;
	private String groupId;
	private String groupTh;
	private String groupEn;
	private String groupDetail;
	private String minorTypeId;
	private String minorTypeTh;
	private String subjectSet;
	private String subjectCodeTh;
	private String subjectCodeEn;
	private String subjectNameTh;
	private String subjectNameEn;
	private String subjectCredit;
	private String subjectTypeTh;
	
}
