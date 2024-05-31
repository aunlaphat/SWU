package com.arg.swu.dto;

import java.io.Serializable;

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
public class MasOccupationGroupData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 3862161739482151878L;
	
	@Schema(description = "pk กลุ่มอาชีพ")
	private Long occupationGroupId;
	@Schema(description = "รหัสกลุ่มอาชีพ")
	private String occupationGroupCode;
	@Schema(description = "ชื่อกลุ่มอาชีพ (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String occupationGroupNameTh;
	@Schema(description = "ชื่อกลุ่มอาชีพ (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String occupationGroupNameEn;
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
