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
public class FinanceReceiptConfigData extends PageableCommon implements Serializable {

    private static final long serialVersionUID = 210905676037595815L;

	@Schema(description = "PK")
    private Long receiptConfigId;
    
    @Schema(description = "path logo ใบเสร็จรับเงิน")
    private String logoPath;
    
    @Schema(description = "ชื่อหน่วยงาน (ไทย)")
    private String depNameTh;
    
    @Schema(description = "ชื่อหน่วยงาน (อังกฤษ)")
    private String depNameEn;
    
    @Schema(description = "ที่อยู่หน่วยงาน (ไทย)")
    private String depAddressTh;
    
    @Schema(description = "ที่อยู่หน่วยงาน (อังกฤษ)")
    private String depAddressEn;
    
    @Schema(description = "เลขประจำตัวผู้เสียภาษีของหน่วยงาน")
    private String depTaxId;
    
    @Schema(description = "prefix เลขใบเสร็จรับเงิน")
    private String receiptPrefix;
    
    @Schema(description = "คำลงท้ายใบเสร็จ (ไทย)")
    private String receiptNoteTh;
    
    @Schema(description = "คำลงท้ายใบเสร็จ (อังกฤษ)")
    private String receiptNoteEn;
    
    @Schema(description = "หมายเหตุใบเสร็จ")
    private String receiptRemark;
    
    @Schema(description = "รูป Signature ผู้รับเงิน")
    private String staffSignaturePath;
    
    @Schema(description = "ชื่อผู้รับเงิน")
    private String staffName;
    
    @Schema(description = "ตำแหน่งผู้ชำระเงิน")
    private String staffPosition;
    
    @Schema(description = "รูป Signature ผู้มีอำนาจลงนาม (ยังไม่ใช้งาน)")
    private String approvedSignaturePath;
    
    @Schema(description = "ชื่อผู้มีอำนาจลงนาม (ยังไม่ใช้งาน)")
    private String approvedName;
    
    @Schema(description = "ตำแหน่งผู้มีอำนาจลงนาม (ยังไม่ใช้งาน)")
    private String approvedPosition;
    
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;

}
