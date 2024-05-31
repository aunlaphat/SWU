package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
public class CoursepublicMediaData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = -7379370142810603070L;

	@Schema(description = "PK")
	private Long coursepublicMediaId;

	@Schema(description = "FK (coursepublic_main)")
	private Long coursepublicId;

	@Schema(description = "รูปแบบสื่อ 30012000")
	private Long mediaType;

	@Schema(description = "ชื่อ Thumbnail (ภาษาไทย)")
	private String mediaNameTh;

	@Schema(description = "ชื่อ Thumbnail (ภาษาอังกฤษ)")
	private String mediaNameEn;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;

	@Schema(description = "ชื่อรอบหลักสูตร (ภาษาไทย)")
	private String publicNameTh;

	@Schema(description = "ชื่อรอบหลักสูตร (ภาษาอังกฤษ)")
	private String publicNameEn;

	@Schema(description = "Popular")
	private Boolean popularStatus;

	@Schema(description = "เผยแพร่บนเว็บไซต์")
	private Boolean publicStatus;

	@Schema(description = "วันที่โปรโมทเริ่มต้น")
	private Date coursePublicStart;

	@Schema(description = "วันที่โปรโมทสิ้นสุด")
	private Date coursePublicEnd;

	@Schema(description = "วันที่เปิดรับสมัคร")
	private Date courseRegisStart;

	@Schema(description = "วันที่ปิดรับสมัคร")
	private Date courseRegisEnd;

	@Schema(description = "รูปแบบการเรียนการสอน")
	private Long courseFormat;

	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาไทย)")
	private String courseFormatDescTh;

	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาอังกฤษ)")
	private String courseFormatDescEn;

	@Schema(description = "การคิดค่าลงทะเบียน 30008000")
	private Long feeStatus;

	@Schema(description = "ค่าลงทะเบียน / ราคา")
	private BigDecimal feeAmount;

	@Schema(description = "ราคาโปรโมชั่น")
	private BigDecimal promotionAmount;
	
	@Schema(description = "Prefix")
	private String prefix;
	 
	@Schema(description = "Module")
	private Long module;
	 
	@Schema(description = "ชื่อไฟล์")
	private String filename;

	@Schema(description = "FK (mas_course_type)")
	private Long courseTypeId;

	@Schema(description = "custom ชื่อประเภทหลักสูตร (ภาษาไทย)")
	private String courseTypeNameTh;
	
	@Schema(description = "custom ชื่อประเภทหลักสูตร (อังกฤษ)")
	private String courseTypeNameEn;
	
	private List<Long> courseTargets;

	private List<Long> courseFormats;

	private Long industryGroupId;

	private String courseKeyword;

	@Schema(description = "custom Fullpath")
	private String fullpath;
	
	@Schema(description = "custom videoType")
	private String videoType;
	
}
