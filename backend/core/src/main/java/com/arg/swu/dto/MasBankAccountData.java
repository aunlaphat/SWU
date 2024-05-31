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
public class MasBankAccountData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 3481542812396167674L;
	
	@Schema(description = "PK")
	private Long bankAccountId;
	
	@Schema(description = "ธนาคาร")
	private Long bankId;
	
	@Schema(description = "สาขา")
	private Long bankBranchId;
	
	@Schema(description = "เลขที่บัญชี", requiredMode = RequiredMode.REQUIRED)
	private String accountNo;
	
	@Schema(description = "ชื่อบัญชี (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String accountNameTh;
	
	@Schema(description = "ชื่อบัญชี (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String accountNameEn;
	
	@Schema(description = "Biller ID")
	private String billerId;
	
	@Schema(description = "Company ID")
	private String companyId;
	
	@Schema(description = "หมายเหตุ")
	private String bankAccountNote;
	
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom ชื่อธนาคาร (ภาษาไทย)")
	private String bankNameTh;
	
	@Schema(description = "custom ชื่อธนาคาร (ภาษาอังกฤษ)")
	private String bankNameEn;
	
	@Schema(description = "custom ชื่อสาขาธนาคาร (ภาษาไทย)")
	private String bankBranchNameTh;
	
	@Schema(description = "custom ชื่อสาขาธนาคาร (ภาษาอังกฤษ)")
	private String bankBranchNameEn;
	
}
