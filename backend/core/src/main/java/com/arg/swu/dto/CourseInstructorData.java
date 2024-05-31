package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CourseInstructorData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -6934685917747011502L;
	
	@Schema(description = "PK")
	private Long courseInstructorId;
	
	@Schema(description = "FK (course_main)" )
	private Long courseId;
	
	@Schema(description = "FK (mas_personal) ")
	private Long instructorId;
	
	@Schema(description = "อาจารย์/ผู้รับผิดชอบหลักสูตร (มีผู้รับผิดชอบหลักได้คนเดียวต่อ 1 หลักสูตร) ", requiredMode = RequiredMode.REQUIRED)
	private Boolean instructorMain;
	
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
	private Long module;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;

	@Schema(description = "custom ชื่ออาจารย์ (ภาษาไทย)")
	private String fullnameTh;
	
	@Schema(description = "custom ชื่ออาจารย์  (ภาษาอังกฤษ)")
	private String fullnameEn;
	
	@Schema(description = "custom อีเมล")
	private String email;

	@Schema(description = "custom รูปคน")
	private String base64;
	
	
}
