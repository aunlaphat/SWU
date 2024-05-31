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
public class CourseRequestAttachData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 7953583328605605562L;

	@Schema(description = "PK")
	private Long courseRequestAttachId;

	@Schema(description = "FK (course_main)")
	private Long courseId;

	@Schema(description = "ชื่อไฟล์ (ภาษาไทย)")
	private String fileNameTh;

	@Schema(description = "ชื่อไฟล์ (ภาษาอังกฤษ)")
	private String fileNameEn;
	
	@Schema(description = "filename")
	private String filename;
	
	@Schema(description = "Prefix")
	private String prefix;
	
	@Schema(description = "Module")
	private Long module;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}