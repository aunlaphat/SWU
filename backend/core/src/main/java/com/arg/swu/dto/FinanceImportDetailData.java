package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sutthiyapakc
 *
 */
@Data
public class FinanceImportDetailData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -6935632294535236734L;

	@Schema(description = "PK")
	private Long detailId;
	
	@Schema(description = "impId")
    private Long impId;
	
	@Schema(description = "Fk (courserpublic_main)")
	private Long coursepublicId;
	
	@Schema(description = "วันที่ชำระเงิน")
	private Date payDatetime;
	
	@Schema(description = "ชื่อผู้เรียน")
	private String memberName;
	
	@Schema(description = "จำนวนเงินที่ชำระ")
	private BigDecimal payAmount;
	
	@Schema(description = "id card")
	private String memberIdCard;

	@Schema(description = "คำนำหน้า sys_prefixname")
	private Long prefixnameId;

	@Schema(description = "คำนำหน้า")
	private String prefix;
	
	@Schema(description = "ชื่อ (ภาษาไทย)")
	private String firstnameTh;
	
	@Schema(description = "นามสกุล (ภาษาไทย)")
	private String lastnameTh;
	
	@Schema(description = "ชื่อ (อังกฤษ)")
	private String firstnameEn;

	@Schema(description = "นามสกุล (อังกฤษ)")
	private String lastnameEn;
	
	@Schema(description = "อีเมล")
	private String email;
	
	@Schema(description = "ที่อยู่")
	private String address;
	
	@Schema(description = "หมายเลขนิติบุคคล")
	private String orgLegalCode;
	
	@Schema(description = "ชื่อนิติบุคคล")
	private String orgName;
	
	@Schema(description = "ที่อยู่นิติบุคคล")
	private String orgAddress;
	
	@Schema(description = "สถานะรายการ pass, fail, miss")
	private String detailStatus;
	
	@Schema(description = "ข้อความ Error")
	private String errorMessage;
	
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	
}
