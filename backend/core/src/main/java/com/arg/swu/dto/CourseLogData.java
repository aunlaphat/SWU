package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CourseLogData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 614896359739192372L;
	
	@Schema(description = "PK")
	private Long courseLogId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "สถานะคำขอเปิดหลักสูตร LOOKUP (30010000) ")
	private Long courseMainStatus;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;

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