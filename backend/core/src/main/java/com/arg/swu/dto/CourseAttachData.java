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
public class CourseAttachData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = -6912891401842397269L;
	
	@Schema(description = "PK auto run (course_attach) ")
	private Long courseAttachId;
	
	@Schema(description = "FK (course_main)" )
	private Long courseId;
	
	@Schema(description = "ชื่อไฟล์ (ภาษาไทย)")
	private String fileNameTh;
	
	@Schema(description = "ชื่อไฟล์ (ภาษาอังกฤษ)")
	private String fileNameEn;
	
	@Schema(description = "FileName")
	private String filename;
	
	@Schema(description = "Prefix")
	private String prefix;
	
	@Schema(description = "Module")
	private Long module;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
