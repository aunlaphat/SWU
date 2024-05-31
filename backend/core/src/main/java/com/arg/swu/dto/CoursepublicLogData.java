package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CoursepublicLogData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -990748283185076623L;
	
	@Schema(description = "PK")
	private Long coursepublicLogId;
	
	@Schema(description = "FK (coursepublic_main)")
	private Long coursepublicId;
	
	@Schema(description = "สถานะเปิดรอบหลักสูตร")
	private Long coursepublicStatus;
	
	@Schema(description = "id ผู้บันทึก")
	private Long createById;
	
	@Schema(description = "วันเวลาที่บันทึก")
	private Date createTimestamp;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
        
    @Schema(description = "เหตุผลการยกเลิก")
    private String cancelReason;
    
    @Schema(description = "custom approverId")
    private Long approverId;
    
    @Schema(description = "custom วันที่สร้าง")
    private Date createDate;

	@Schema(description = "custom ชื่อ (ภาษาไทย)")
	private String fullnameTh;
	
	@Schema(description = "custom ชื่อ  (ภาษาอังกฤษ)")
	private String fullnameEn;

	@Schema(description = "custom สถานะคำขออนุมัติ  (ภาษาไทย)")
	private String statusNameTh;

	@Schema(description = "custom สถานะคำขออนุมัติ  (ภาษาอังกฤษ)")
	private String statusNameEn;
	
}
