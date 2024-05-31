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
public class SwuSubjectData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 7189578294423436642L;

	@Schema(description = "PK")
	private Long subjectSwuId;
	
	@Schema(description = "รหัสหลักสูตร FK swu_curriculum")
	private String curriculumSwuId;
	
	@Schema(description = "รหัสหลักสูตร สป.อว.")
	private String curriculumMhesiId;
	
	@Schema(description = "รหัสกลุ่มหมวด")
	private String groupId;
	
	@Schema(description = "ชื่อกลุ่มหมวด (ภาษาไทย)")
	private String groupTh;
	
	@Schema(description = "ชื่อกลุ่มหมวด (ภาษาอังกฤษ)")
	private String groupEn;
	
	@Schema(description = "กลุ่มวิชา")
	private String groupDetail;
	
	@Schema(description = "รหัสแผนการศึกษา")
	private String minorTypeId;
	
	@Schema(description = "ชื่อแผนการศึกษา (ภาษาไทย)")
	private String minorTypeTh;
	
	@Schema(description = "ชุดวิชา")
	private String subjectSet;
	
	@Schema(description = "รหัสวิชา (ภาษาไทย)")
	private String subjectCodeTh;
	
	@Schema(description = "รหัสวิชา (ภาษาอังกฤษ)")
	private String subjectCodeEn;
	
	@Schema(description = "ชื่อรายวิชา (ภาษาไทย)")
	private String subjectNameTh;
	
	@Schema(description = "ชื่อรายวิชา (ภาษาอังกฤษ)")
	private String subjectNameEn;
	
	@Schema(description = "จํานวนหน่วยกิต")
	private BigDecimal subjectCredit;
	
	@Schema(description = "ประเภทวิชา (ถาษาไทย)")
	private String subjectTypeTh;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
