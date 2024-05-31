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
public class MasBankData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 2997731096720360658L;
	
	@Schema(description = "pk ธนาคาร")
	private Long bankId;
	@Schema(description = "รหัสธนาคาร", requiredMode = RequiredMode.REQUIRED)
	private String bankCode;
	@Schema(description = "ชื่อธนาคาร (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String bankNameTh;
	@Schema(description = "ชื่อธนาคาร (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String bankNameEn;
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
