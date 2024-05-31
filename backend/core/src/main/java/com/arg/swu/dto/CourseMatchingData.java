package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CourseMatchingData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 7018776743417239044L;
	
	@Schema(description = "PK")
	private Long courseMatchingId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "FK (swu_subject)")
	private Long subjectSwuId;
	
	@Schema(description = "บุคลากรภายนอก")
	private Boolean instructorType;
	
	@Schema(description = "ชื่ออาจารย์/ผู้รับผิดชอบ (ภาษาไทย)")
	private String externalNameTh;
	
	@Schema(description = "ชื่ออาจารย์/ผู้รับผิดชอบ  (ภาษาอังกฤษ)")
	private String externalNameEn;
	
	@Schema(description = "อีเมล")
	private String externalEmail;
	
	@Schema(description = "FileName")
	private String filename;
	
	@Schema(description = "Prefix")
	private String prefix;
	
	@Schema(description = "Module")
	private String module;
	
	@Schema(description = "FK swu_curriculum")
	private String curriculumSwuId;

	@Schema(description = "รหัสวิชา (ภาษาไทย)")
	private String subjectCodeTh;

	@Schema(description = "รหัสวิชา (ภาษาอังกฤษ)")
	private String subjectCodeEn;
	
	@Schema(description = "ชื่อรายวิชา (ภาษาไทย)")
	private String subjectNameTh;

	@Schema(description = "ชื่อรายวิชา (ภาษาอังกฤษ)")
	private String subjectNameEn;
	
	@Schema(description = "ชื่อหลักสูตร (ภาษาไทย)")
	private String curriculumNameTh;

	@Schema(description = "ชื่อหลักสูตร (ภาษาอังกฤษ)")
	private String curriculumNameEn;
	
	@Schema(description = "จํานวนหน่วยกิต")
	private BigDecimal subjectCredit;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	private String ownerDepNameTh;
	private String ownerDepNameEn;
	private String subjectSet;
}