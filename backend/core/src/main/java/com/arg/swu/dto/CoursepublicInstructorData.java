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
public class CoursepublicInstructorData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 6377385005551227060L;
	
	@Schema(description = "PK")
	private Long coursepublicInstructorId;

	@Schema(description = "FK (coursepublic_main)")
	private Long coursepublicId;

	@Schema(description = "FK (mas_personal)")
	private Long instructorId;

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

	@Schema(description = "อาจารย์/ผู้รับผิดชอบหลักสูตร")
	private Boolean instructorMain;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom ชื่ออาจารย์ (ภาษาไทย)")
	private String fullnameTh;
	
	@Schema(description = "custom ชื่ออาจารย์  (ภาษาอังกฤษ)")
	private String fullnameEn;
	
	@Schema(description = "custom อีเมล")
	private String email;

	@Schema(description = "ชื่อย่อคำนำหน้า (ไทย)")
    private String prefixShortTh;

    @Schema(description = "ชื่อย่อคำนำหน้า (อังกฤษ)")
    private String prefixShortEn;

	@Schema(description = "base64")
	private String base64;
	
}
