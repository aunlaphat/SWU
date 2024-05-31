package com.arg.swu.dto;

import java.io.Serializable;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class MasGeneralSkillData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 5935493896567032096L;
	
	@Schema(description = "pk ทักษะทั่วไป")
	private Long generalSkillId;
	
	@Schema(description = "รหัสทักษะทั่วไป")
	private String generalSkillCode;
	
	@Schema(description = "ชื่อทักษะทั่วไป (ไทย)", requiredMode = RequiredMode.REQUIRED)
	private String generalSkillNameTh;
	
	@Schema(description = "ชื่อทักษะทั่วไป (อังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String generalSkillNameEn;
	
	@Schema(description = "Prefix")
	private String prefix;
	  
	@Schema(description = "Module")
	private Long module;
	  
	@Schema(description = "ชื่อไฟล์")
	private String filename;
	 
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	@Schema(description = "รูปแบนเนอร์skill")
	private String skillImage;
	
	@Schema(description = "รูปแบบหมวดหมู่ทักษะ")
	private Long skillFormat;
	
	@Schema(description = "custom รูปแบบหมวดหมู่ (ภาษาไทย)")
	private String skillFormatTh;
	
	@Schema(description = "custom รูปแบบหมวดหมู่ (ภาษาอังกฤษ)")
	private String skillFormatEn;
	
	@Schema(description = "custom รูปแบนเนอร์รูปแบบหมวดหมู่")
	private String base64;
	
	@Schema(description = "custom (mas_generalskill_level)")
	private List<MasGeneralSkillLevelData> skillLevelList;

}
