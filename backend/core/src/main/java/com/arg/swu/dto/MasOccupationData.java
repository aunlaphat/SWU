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
public class MasOccupationData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -1560231884805866415L;

    @Schema(description = "ข้อมูลอาชีพ")
    private Long occupationId;
    
    @Schema(description = "กลุ่มอาชีพ")
    private Long occupationGroupId;
    
    @Schema(description = "รหัสอาชีพ", requiredMode = RequiredMode.REQUIRED)
    private String occupationCode;
    
    @Schema(description = "ชื่ออาชีพ (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
    private String occupationNameTh;
    
    @Schema(description = "ชื่ออาชีพ (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
    private String occupationNameEn;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
    @Schema(description = "custom ชื่อกลุ่มอาชีพ (ภาษาไทย)")
    private String occupationGroupNameTh;

    @Schema(description = "custom ชื่อกลุ่มอาชีพ (ภาษาอังกฤษ)")
    private String occupationGroupNameEn;

    @Schema(description = "custom ประเภททักษะ (ภาษาอังกฤษ)")
    private List<MasOccupationSkillData> occupationSkills;
	
}
