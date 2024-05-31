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
public class CoursepublicAttachData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -7333338419751908371L;

	@Schema(description = "PK")
	private Long coursepublicAttachId;
	
	@Schema(description = "FK (coursepublic_main)")
	private Long coursepublicId;

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
