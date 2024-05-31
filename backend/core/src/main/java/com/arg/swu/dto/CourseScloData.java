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
public class CourseScloData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = -6095986768520932250L;

	@Schema(description = "PK")
	private Long courseScloId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "รหัส SCLO")
	private String courseScloCode;
	
	@Schema(description = "ผลลัพธ์การเรียนรู้ของหลักสูตรอบรมระยะสั้น")
	private String courseScloDesc;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}