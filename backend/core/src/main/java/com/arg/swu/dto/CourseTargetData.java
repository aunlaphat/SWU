package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CourseTargetData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 8359058219533494739L;
	
	@Schema(description = "PK")
	private Long courseTargetId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "กลุ่มเป้าหมาย LOOKUP (30002000) ")
	private Long targetGroupId;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}