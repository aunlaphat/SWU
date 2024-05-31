package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class MasBankChargeData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -1857873283691794300L;

    @Schema(description = "PK")
    private Long chargeId;
    
    @Schema(description = "ประเภทรูปแบบการชำระงิน LOOKUP (30023000)")
    private Long paymentType;
    
    @Schema(description = "ประเภทรายการ/บัตร LOOKUP (30024000)")
    private Long cardType;
    
    @Schema(description = "อัตราค่าธรรมเนียม")
    private BigDecimal chargeRate;
    
    @Schema(description = "เรียกเก็บมหาวิทยาลัย")
    private BigDecimal universityChargePercent;
    
    @Schema(description = "เรียกเก็บผู้เรียน")
    private BigDecimal studentChargePercent;
    
    @Schema(description = "ปีที่เริ่มใช้งาน")
    private Long startYear;
    
	@Schema(description = "custom ผู้บันทึก")
	private Long createById;
    
	@Schema(description = "custom วันที่สร้าง")
	private Date createDate;
    
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom ปีที่เริ่มใช้งานใช้ในการค้นหา")
	private List<Date> startYearList;
	
	@Schema(description = "custom วันที่สร้างใช้ในการค้นหา")
	private List<Date> createDateList;

	@Schema(description = "custom รูปแบบการชำระเงิน (ภาษาไทย)")
	private String paymentTypeNameTh;
	
	@Schema(description = "custom รูปแบบการชำระเงิน (ภาษาอังกฤษ)")
	private String paymentTypeNameEn;
	
	@Schema(description = "custom รายการบัตร (ภาษาไทย)")
	private String cardTypeNameTh;
	
	@Schema(description = "custom รายการบัตร (ภาษาอังกฤษ)")
	private String cardTypeNameEn;


}
