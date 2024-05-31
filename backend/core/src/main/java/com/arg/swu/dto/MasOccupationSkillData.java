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
public class MasOccupationSkillData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 6508662164840784562L;

	@Schema(description = "PK")
	private Long occSkillId;

	@Schema(description = "FK อาชีพ mas_occupation")
	private Long occupationId;

	@Schema(description = "FK ทักษะ mas_general_skill")
	private Long generalSkillId;

	@Schema(description = "ระดับ LOOKUP (30011000) ")
	private Long occSkillLevel;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom ระดับ (ภาษาไทย) ")
	private String levelNameTh;
	
	@Schema(description = "custom ระดับ (ภาษาอังกฤษ) ")
	private String levelNameEn;
	
	@Schema(description = "custom ทักษะ (ภาษาไทย) ")
	private String generalSkillNameTh;
	
	@Schema(description = "custom ทักษะ (ภาษาอังกฤษ) ")
	private String generalSkillNameEn;

}
