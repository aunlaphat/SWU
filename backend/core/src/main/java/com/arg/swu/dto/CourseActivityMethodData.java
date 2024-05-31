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
public class CourseActivityMethodData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 4084341230353300434L;

	@Schema(description = "PK")
	private Long courseActivityMethodId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "FK (course_activity)")
	private Long coruseActivityId;

	@Schema(description = "FK (mas_course_method)")
	private Long courseMethodId;

	@Schema(description = "รายละเอียดเพิ่มเติม")
	private String courseMethodDetail;
	
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
}
