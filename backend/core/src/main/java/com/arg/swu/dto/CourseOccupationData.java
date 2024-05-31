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
public class CourseOccupationData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -6664703896353779156L;
	
	@Schema(description = "PK")
	private Long courseOccupationId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "FK (mas_occupation)")
	private Long occupationId;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
