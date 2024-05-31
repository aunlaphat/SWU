package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

/**
 * 
 * @author sutthiyapakc
 *
 */
@Data
public class FinancePaymentData extends PageableCommon implements Serializable {
	

	private static final long serialVersionUID = 3655757737942175297L;
	
	@Schema(description = "PK")
	private Long paymentId;
	
	@Schema(description = "บัญชีเงินฝากที่รับเงิน")
    private Long bankAccountId;

	@Schema(description = "ประเภทรูปแบบการชำระเงิน", requiredMode = RequiredMode.REQUIRED)
	private Long paymentType;
    
	@Schema(description = "ประเภทรายการ/บัตร", requiredMode = RequiredMode.REQUIRED)
	private Long cardType;
	
	@Schema(description = "fk member_course  = member_course_id")
    private Long memberCourseId;
	
	@Schema(description = "fk coursepublic_main = coursepublic_id")
    private Long coursepublicId;
	
	@Schema(description = "fk mas_member.member_id")
    private Long memberId;
    
	@Schema(description = "จำนวนเงิน")
	private BigDecimal paymentAmount;

	@Schema(description = "Ref1")
	private String ref1;

	@Schema(description = "Ref2")
	private String ref2;

	@Schema(description = "สถานะการชำระเงิน")
	private Long paymentStatus;

	@Schema(description = "วันที่ชำระเงิน")
	private Date receiptDate;

	@Schema(description = "Transaction ID")
	private String transactionId;

	@Schema(description = "Transaction Datetime")
	private Date transactionDatetime;

	@Schema(description = "Transaction IP")
	private String transactionIp;

	@Schema(description = "Transaction OS")
	private String transactionOs;

	@Schema(description = "Transaction Browser")
	private String transactionBrowser;

	@Schema(description = "Transaction Agent")
	private String transactionAgent;

	@Schema(description = "Raw Data to Bank")
	private String transactionRawData;

	@Schema(description = "Transaction Pay Account")
	private String transactionPayAccount;

	@Schema(description = "Path file original ใบเสร็จ ที่ยังไม่เข้ารหัส CA")
	private String receiptOriginalNonCaPath;

	@Schema(description = "Path file original ใบเสร็จ ที่เข้ารหัส CA แล้ว")
	private String receiptOriginalCaPath;
        	
	@Schema(description = "Path file copy ใบเสร็จ ที่ยังไม่เข้ารหัส CA")
        private String receiptCopyNonCaPath;
        
        @Schema(description = "Path file copy ใบเสร็จ ที่เข้ารหัส CA แล้ว")
	private String receiptCopyCaPath;
        
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom วันที่ชำระเงินใช้ในการค้นหา")
	private List<Date> receiptDateList;

	@Schema(description = "custom ประเภทรูปแบบการชำระเงิน (ภาษาไทย)")
	private String paymentTypeTh;
	
	@Schema(description = "custom ประเภทรูปแบบการชำระเงิน (อังกฤษ)")
	private String paymentTypeEn;
	
	@Schema(description = "custom courseCode")
	private String courseCode;
	
	@Schema(description = "custom ชื่อหลักสูตร(ภาษาไทย)")
	private String publicNameTh;
	
	@Schema(description = "custom ชื่อหลักสูตร(อังกฤษ)")
	private String publicNameEn;
	
	@Schema(description = "custom ชื่อ(ภาษาไทย)")
	private String memberFirstnameTh;
	
	@Schema(description = "custom ชื่อ(อังกฤษ)")
	private String memberFirstnameEn;

	@Schema(description = "custom ชื่อ(ภาษาไทย)")
	private String memberLastnameTh;
	
	@Schema(description = "custom ชื่อ(อังกฤษ)")
	private String memberLastnameEn;
	
	@Schema(description = "เลขที่ใบเสร็จ")
	private String receiptNo;
	
	@Schema(description = "fk อ้างอิงการนำเข้า finance_import_detail.detail_id")
	private Long detailImpId;
	
	@Schema(description = "chargeId")
	private String chargeId;

    @Schema(description = "studyStatus")
    private Long studyStatus;
        
    @Schema(description = "courseId")
	private Long courseId;

    @Schema(description = "memberReceiptViewFlag")
    private Boolean memberReceiptViewFlag;
}
