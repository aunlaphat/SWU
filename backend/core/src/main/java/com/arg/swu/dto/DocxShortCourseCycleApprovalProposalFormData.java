package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DocxShortCourseCycleApprovalProposalFormData implements Serializable {
	
	private static final long serialVersionUID = -7232483059448853889L;

	@Schema(description = "PK")
	private Long coursepublicId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "ชื่อหลักสูตร (ไทย)")
	private String courseNameTh;
	
	@Schema(description = "ชื่อหลักสูตร (อังกฤษ)")
	private String courseNameEn;

	@Schema(description = "ชื่อรอบหลักสูตร (ภาษาไทย)")
	private String publicNameTh;
	
	@Schema(description = "ชื่อรอบหลักสูตร (อังกฤษ)")
	private String publicNameEn;
	
	@Schema(description = "คำอธิบายรายวิชาของหลักสูตร (ภาษาไทย)")
	private String courseDescTh;
	
	@Schema(description = "คำอธิบายรายวิชาของหลักสูตร (ภาษาอังกฤษ)")
	private String courseDescEn;

	@Schema(description = "จำนวนที่เปิดรับ")
	private Long maxEnroll;
	
	@Schema(description = "วันที่เปิดรับสมัคร")
	private Date courseRegisStart;

	@Schema(description = "วันที่ปิดรับสมัคร")
	private Date courseRegisEnd;

	@Schema(description = "วันที่เรียนเริ่มต้น")
	private Date courseClassStart;

	@Schema(description = "วันที่เรียนสิ้นสุด")
	private Date courseClassEnd;

	@Schema(description = "รูปแบบการเรียนการสอน")
	private Long courseFormat;
	
	@Schema(description = "รูปแบบการเรียนการสอน (ภาษาไทย)")
	private String courseFormatTh;
	
	@Schema(description = "รูปแบบการเรียนการสอน (ภาษาอังกฤษ)")
	private String courseFormatEn;

	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาไทย)")
	private String courseFormatDescTh;

	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาอังกฤษ)")
	private String courseFormatDescEn;
	
	@Schema(description = "เวลาเรียน (ภาษาไทย)")
	private String courseTimeTh;

	@Schema(description = "เวลาเรียน (ภาษาอังกฤษ)")
	private String courseTimeEn;
	
	@Schema(description = "การคิดค่าลงทะเบียน 30008000")
	private Long feeStatus;
	
	@Schema(description = "การคิดค่าลงทะเบียน (ภาษาไทย)")
	private String feeStatusTh;
	
	@Schema(description = "การคิดค่าลงทะเบียน (ภาษาอังกฤษ)")
	private String feeStatusEn;
	
	@Schema(description = "ค่าลงทะเบียน / ราคา")
	private BigDecimal feeAmount;

	@Schema(description = "ราคาโปรโมชั่น")
	private BigDecimal promotionAmount;

	@Schema(description = "อีเมล")
	private String email;

	@Schema(description = "ชื่ออาจารย์ผู้รับผิดชอบหลัก (ภาษาไทย)")
	private String instructorNameTh;
	
	@Schema(description = "ชื่ออาจารย์ผู้รับผิดชอบหลัก (ภาษาอังกฤษ)")
	private String instructorNameEn;
	
}
