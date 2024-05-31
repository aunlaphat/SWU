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
public class CourseSkillData extends PageableCommon implements Serializable {

	
	private static final long serialVersionUID = 3618720669603392694L;

	@Schema(description = "PK auto run ")
	private Long courseSkillId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "หมวดหมู่ทักษะ")
	private Long generalSkillId;
	
	@Schema(description = "อื่นๆ")
	private Boolean courseSkillOtherStatus;
	
	@Schema(description = "ชื่อทักษะอื่น (ไทย)")
	private String courseSkillOtherNameTh;
	
	@Schema(description = "ชื่อทักษะอื่น (อังกฤษ)")
	private String courseSkillOtherNameEn;
	
	@Schema(description = "ระดับ")
	private Long skillLevel;
	
	@Schema(description = "น้ำหนัก")
	private BigDecimal skillWeight;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom ชื่อทักษะ (ไทย)")
	private String generalSkillNameTh;
	
	@Schema(description = "custom ชื่อทักษะ (อังกฤษ)")
	private String generalSkillNameEn;
	
	@Schema(description = "custom ระดับ (ไทย)")
	private String levelNameTh;
	
	@Schema(description = "custom ระดับ (อังกฤษ)")
	private String levelNameEn;
	
}
