package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MasCampaignData  implements Serializable {
    
    private static final long serialVersionUID = 2997731096722360631L;

    @Schema(description = "pk รหัสแคมเปญ")
    private Long campaignId;
    @Schema(description = "รหัสแคมเปญ")
    private String campaignCode;
    @Schema(description = "ชื่อแคมเปญ (ไทย)")
    private String campaignNameTh;
    @Schema(description = "ชื่อแคมเปญ (อังกฤษ)")
    private String campaignNameEn;
    @Schema(description = "วันที่เริ่มต้น")
    private Date startDate;
    @Schema(description = "วันที่สิ้นสุด")
    private Date endDate;
    @Schema(description = "จำนวนคูปอง")
    private Long campaignFull;
    @Schema(description = "รูปแบบส่วนลด")
    private Long discountFormat;
    @Schema(description = "ส่วนลด (เปอร์เซ็น)")
    private BigDecimal discountRate;
    @Schema(description = "กำหนดจำนวนเงินสูงสุด")
    private Boolean maxDiscountStatus;
    @Schema(description = "จำนวนเงินลดสูงสุด")
    private BigDecimal maxDiscountAmount;
    @Schema(description = "ส่วนลด (บาท)")
    private BigDecimal discountAmount;
    @Schema(description = "จำนวนเงินขั้นต่ำที่สามารถใช้ส่วนลดได้")
    private BigDecimal minCoursePrice;
    @Schema(description = "หลักสูตรที่เปิดสอนทั้งหมด")
    private Boolean allCourse;
    @Schema(description = "สถานะ")
    private Boolean activeFlag;

}
