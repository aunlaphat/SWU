package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class SwuCurriculumData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -2723392053730994161L;
	
	@Schema(description = "PK รหัสหลักสูตร มศว Not Autorun ")
	private String curriculumSwuId;
	
	@Schema(description = "รหัสหลักสูตร สป.อว.")
	private String curriculumMhesiId;
	
	@Schema(description = "ชื่อหลักสูตร (ภาษาไทย)")
	private String curriculumNameTh;
	
	@Schema(description = "ชื่อหลักสูตร (ภาษาอังกฤษ)")
	private String curriculumNameEn;
	
	@Schema(description = "จำนวนหน่วยกิตรวมตลอดหลักสูตร")
	private BigDecimal creditStudy;
	
	@Schema(description = "รหัสวุฒิการศึกษา")
	private String degreeCode;
	
	@Schema(description = "วุฒิการศึกษาแบบย่อ (ภาษาไทย)")
	private String degreeShortTh;
	
	@Schema(description = "วุฒิการศึกษา (ภาษาไทย)")
	private String degreeNameTh;
	
	@Schema(description = "วุฒิการศึกษาแบบย่อ (ภาษาอังกฤษ)")
	private String degreeShortEn;
	
	@Schema(description = "วุฒิการศึกษา (ภาษาอังกฤษ)")
	private String degreeNameEn;
	
	@Schema(description = "รหัสสาขาวิชา")
	private String majorCode;
	
	@Schema(description = "ชื่อสาขาวิชา (ภาษาไทย)")
	private String majorNameTh;
	
	@Schema(description = "ชื่อสาขาวิชา (ภาษาอังกฤษ)")
	private String majorNameEn;
	
	@Schema(description = "รหัสระดับการศึกษา")
	private String eduLevelCode;
	
	@Schema(description = "ชื่อระดับการศึกษา (ภาษาไทย)")
	private String eduLevelNameTh;
	
	@Schema(description = "ชื่อระดับการศึกษา (ภาษาอังกฤษ)")
	private String eduLevelNameEn;
	
	@Schema(description = "จำนวนปีที่ศึกษา")
	private BigDecimal studyYear;
	
	@Schema(description = "รหัสหน่วยงานเจ้าของหลักสูตร ref mas_department.dep_code")
	private String depCode;
	
	@Schema(description = "custom FK (swu_subject)")
	private Long subjectSwuId;
	
	@Schema(description = "custom รหัสกลุ่มหมวด")
	private String groupId;
	
	@Schema(description = "custom ชื่อกลุ่มหมวด (ภาษาไทย)")
	private String groupTh;
	
	@Schema(description = "custom ชื่อกลุ่มหมวด (ภาษาอังกฤษ)")
	private String groupEn;
	
	@Schema(description = "custom กลุ่มวิชา")
	private String groupDetail;
	
	@Schema(description = "custom รหัสแผนการศึกษา")
	private String minorTypeId;
	
	@Schema(description = "custom ชื่อแผนการศึกษา (ภาษาไทย)")
	private String minorTypeTh;
	
	@Schema(description = "custom ชุดวิชา")
	private String subjectSet;
	
	@Schema(description = "custom รหัสวิชา (ภาษาไทย)")
	private String subjectCodeTh;
	
	@Schema(description = "custom รหัสวิชา (ภาษาอังกฤษ)")
	private String subjectCodeEn;
	
	@Schema(description = "custom ชื่อรายวิชา (ภาษาไทย)")
	private String subjectNameTh;
	
	@Schema(description = "custom ชื่อรายวิชา (ภาษาอังกฤษ)")
	private String subjectNameEn;
	
	@Schema(description = "custom จํานวนหน่วยกิต")
	private BigDecimal subjectCredit;
	
	@Schema(description = "custom ประเภทวิชา (ถาษาไทย)")
	private String subjectTypeTh;
	
	@Schema(description = "custom KEY ")
	private String code;
	
	@Schema(description = "custon ids")
	private List<Long> subjectSwuIds;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;


	private Long chairmanId;
	private String ownerDepCode;
	private String ownerDepNameTh;
	private String ownerDepNameEn;
	private String ownerFacultyCode;
	private String ownerFacultyNameTh;
	private String ownerFacultyNameEn;
	
}