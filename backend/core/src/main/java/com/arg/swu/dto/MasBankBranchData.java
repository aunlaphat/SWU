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
public class MasBankBranchData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 2068079896787533999L;
	
	@Schema(description = "pk สาขาธนาคาร")
	private Long bankBranchId;
	@Schema(description = "fk ธนาคาร", requiredMode = RequiredMode.REQUIRED)
	private Long bankId;
	@Schema(description = "รหัสสาขา", requiredMode = RequiredMode.REQUIRED)
	private String bankBranchCode;
	@Schema(description = "ชื่อสาขา (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String bankBranchNameTh;
	@Schema(description = "ชื่อสาขา (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String bankBranchNameEn;
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom ชื่อธนาคาร (ภาษาไทย)")
	private String bankNameTh;
	@Schema(description = "custom ชื่อธนาคาร (ภาษาอังกฤษ)")
	private String bankNameEn;
	
}
