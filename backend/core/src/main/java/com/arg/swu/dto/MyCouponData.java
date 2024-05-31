package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MyCouponData implements Serializable {

    private static final long serialVersionUID = 1775621082566360510L;

    /*Request */
    @Schema(description = "fk member_info.member_id เลขประจำตัวผู้ลงทะเบียนเรียน")
    private Long memberId;
 
    /* Response */
    @Schema(description = "member Coupon Id")
    private Long memberCouponId;   

    @Schema(description = "campaign Id")
    private Long campaignId;   

    @Schema(description = "fk การลงทะเบียนหลักสูตร ที่ถูกใช้งาน")
    private Long memberCourseId;  

    @Schema(description = "สถานะกรใช้ code")
    private Boolean usageStatus;  
    
    @Schema(description = "วันเวลาที่ถูกใช้งาน")
    private Date usageTimestamp;  
    
    @Schema(description = "สถานะหมดอายุ")
    private Boolean expireStatus;  
    
    @Schema(description = "วันหมดอายุ")
    private Date expireDate;  
        
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

    @Schema(description = "หลักสูตรที่เปิดสอนทั้งหมด")
    private Boolean allCourse;  
   
}
