package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CourseMainData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -5850670059201638142L;
	
	@Schema(description = "PK")
	private Long courseId;
	
	@Schema(description = "Course Reference ID")
	private Long courseRefId;
	
	@Schema(description = "Course Version")
	private Long courseVersion;
	
	@Schema(description = "สถานภาพของหลักสูตรอบรมระยะสั้น LOOKUP (30022000)")
	private Long courseNewStatus;
	
	@Schema(description = "FK (mas_course_type)")
	private Long courseTypeId;
	
	@Schema(description = "FK (mas_department)")
	private Long depIdLevel1;
	
	@Schema(description = "FK (mas_department)")
	private Long depIdLevel2;
	
	@Schema(description = "รหัสหลักสูตร", requiredMode = RequiredMode.REQUIRED)
	private String courseCode;
	
	@Schema(description = "ชื่อหลักสูตร (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String courseNameTh;
	
	@Schema(description = "ชื่อหลักสูตร (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String courseNameEn;
	
	@Schema(description = "คำอธิบายรายวิชาของหลักสูตร (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String courseDescTh;
	
	@Schema(description = "คำอธิบายรายวิชาของหลักสูตร (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String courseDescEn;
	
	@Schema(description = "ชื่อ Tag")
	private String[] courseHashtag;
	
	@Schema(description = "สถานะคำขอเปิดหลักสูตร LOOKUP (30010000)", requiredMode = RequiredMode.REQUIRED)
	private Long courseMainStatus;
	
	@Schema(description = "สถานะการเพิ่มแบบ Break Rule (ไม่ต้องอนุมัติ)", requiredMode = RequiredMode.REQUIRED)
	private Boolean forceStatus;
	
	@Schema(description = "รูปแบบการเรียนการสอน LOOKUP (30003000)", requiredMode = RequiredMode.REQUIRED)
	private Long courseFormat;
	
	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String courseFormatDescTh;
	
	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String courseFormatDescEn;
	
	@Schema(description = "จำนวนชั่วโมงการเรียนทฤษฏี", requiredMode = RequiredMode.REQUIRED)
	private BigDecimal courseTheoryH;
	
	@Schema(description = "จำนวนชั่วโมงการเรียนปฏิบัติ", requiredMode = RequiredMode.REQUIRED)
	private BigDecimal courseActionH;
	
	@Schema(description = "รวมจำนวนชั่วโมงเรียน", requiredMode = RequiredMode.REQUIRED)
	private BigDecimal courseTotalH;
	
	@Schema(description = "ระยะเวลาเรียนตลอดหลักสูตรอบรมระยะสั้น", requiredMode = RequiredMode.REQUIRED)
	private BigDecimal courseDurationTime;
	
	@Schema(description = "หน่วยเวลา LOOKUP (30004000)", requiredMode = RequiredMode.REQUIRED)
	private Long durationTimeUnit;
	
	@Schema(description = "รูปแบบผลการเรียน LOOKUP (30005000)", requiredMode = RequiredMode.REQUIRED)
	private Long gradeFormat;
	
	@Schema(description = "จำนวนหน่วยกิต", requiredMode = RequiredMode.REQUIRED)
	private BigDecimal creditAmount;
	
	@Schema(description = "กลุ่มอุตสาหกรรม/กลุ่มพัฒนาคน LOOKUP (30001000)", requiredMode = RequiredMode.REQUIRED)
	private Long industryGroupId;
	
	@Schema(description = "กลุ่มเป้าหมายอื่นๆ", requiredMode = RequiredMode.REQUIRED)
	private Boolean targetGroupOtherStatus;
	
	@Schema(description = "ชื่อกลุ่มเป้าหมายอื่น", requiredMode = RequiredMode.REQUIRED)
	private String targetGroupOtherName;
	
	@Schema(description = "ความรู้พื้นฐาน/คุณสมบัติของผู้เข้าอบรม  (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
	private String courseSpecificRequirementTh;
	
	@Schema(description = "ความรู้พื้นฐาน/คุณสมบัติของผู้เข้าอบรม  (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
	private String courseSpecificRequirementEn;

	@Schema(description = "วันที่ขออนุมัติ")
	private Date requestDate;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom รูปแบบการเรียนการสอน (ภาษาไทย)")
	private String courseFormatTh;
	
	@Schema(description = "custom รูปแบบการเรียนการสอน (ภาษาอังกฤษ)")
	private String courseFormatEn;
	
	@Schema(description = "custom สถานะคำขอเปิดหลักสูตร (ภาษาไทย)")
	private String courseMainStatusTh;
	
	@Schema(description = "custom สถานะคำขอเปิดหลักสูตร (ภาษาอังกฤษ)")
	private String courseMainStatusEn;
	
	@Schema(description = "custom สถานภาพของหลักสูตรอบรมระยะสั้น (ภาษาไทย)")
	private String courseNewStatusTh;
	
	@Schema(description = "custom สถานภาพของหลักสูตรอบรมระยะสั้น (ภาษาอังกฤษ)")
	private String courseNewStatusEn;
	
	@Schema(description = "custom หน่วยเวลา (ภาษาไทย)")
	private String durationTimeUnitTh;
	
	@Schema(description = "custom หน่วยเวลา (ภาษาอังกฤษ)")
	private String DurationTimeUnitEn;
	
	@Schema(description = "custom รูปแบบผลการเรียน (ภาษาไทย)")
	private String gradeFormatTh;
	
	@Schema(description = "custom รูปแบบผลการเรียน (ภาษาอังกฤษ)")
	private String GradeFormatEn;
	
	@Schema(description = "custom กลุ่มอุตสาหกรรม/กลุ่มพัฒนาคน (ภาษาไทย)")
	private String industryGroupTh;
	
	@Schema(description = "custom กลุ่มอุตสาหกรรม/กลุ่มพัฒนาคน (ภาษาอังกฤษ)")
	private String industryGroupEn;
	
	@Schema(description = "custom รหัสประเภทหลักสูตร ")
	private String courseTypeCode;
	
	@Schema(description = "custom ชื่อประเภทหลักสูตร (ภาษาไทย)")
	private String courseTypeNameTh;
	
	@Schema(description = "custom ชื่อประเภทหลักสูตร (อังกฤษ)")
	private String courseTypeNameEn;
	
	@Schema(description = "custom รหัสคณะ/หน่วยงาน Level1")
	private String depCodeLevel1;
	
	@Schema(description = "custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาไทย) Level1")
	private String depNameThLevel1;
	
	@Schema(description = "custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาอังกฤษ) Level1")
	private String depNameEnLevel1;
	
	@Schema(description = "custom ชื่อย่อคณะ/หน่วยงาน (ภาษาไทย) Level1")
	private String depNameShortThLevel1;
	
	@Schema(description = "custom ชื่อย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level1")
	private String depNameShortEnLevel1;
	
	@Schema(description = "custom ตัวย่อคณะ/หน่วยงาน (ภาษาไทย) Level1")
	private String depNameAbbrThLevel1;
	
	@Schema(description = "custom ตัวย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level1")
	private String depNameAbbrEnLevel1;
	
	@Schema(description = "custom รหัสคณะ/หน่วยงาน Level2")
	private String depCodeLevel2;
	
	@Schema(description = "custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาไทย) Level2")
	private String depNameThLevel2;
	
	@Schema(description = "custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาอังกฤษ) Level2")
	private String depNameEnLevel2;
	
	@Schema(description = "custom ชื่อย่อคณะ/หน่วยงาน (ภาษาไทย) Level2")
	private String depNameShortThLevel2;
	
	@Schema(description = "custom ชื่อย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level2")
	private String depNameShortEnLevel2;
	
	@Schema(description = "custom ตัวย่อคณะ/หน่วยงาน (ภาษาไทย) Level2")
	private String depNameAbbrThLevel2;
	
	@Schema(description = "custom ตัวย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level2")
	private String depNameAbbrEnLevel2;
	
	@Schema(description = "custom ผู้บันทึก (ภาษาไทย)")
	private String saveByFullNameTh;
	
	@Schema(description = "custom ผู้บันทึก (ภาษาอังกฤษ)")
	private String saveByFullNameEn;

	@Schema(description = "custom เวลาที่บันทึก")
	private Date createDate;

	@Schema(description = "custom ผู้ที่บันทึก")
	private Long createById;

    @Schema(description = "custom การเทียบเคียงหลักสูตร (ภาษาไทย)")
    private String mappingStatusTh;

    @Schema(description = "custom การเทียบเคียงหลักสูตร (ภาษาอังกฤษ)")
    private String mappingStatusEn;

	@Schema(description = "custom เป้าหมาย (ภาษาไทย)")
	private String[] targetTh;
	
	@Schema(description = "custom เป้าหมาย (ภาษาอังกฤษ)")
	private String[] targetEn;

	@Schema(description = "custom อาชีพ (ภาษาไทย)")
	private String[] occupationTh;
	
	@Schema(description = "custom อาชีพ (ภาษาอังกฤษ)")
	private String[] occupationEn;
	
	@Schema(description = "custom count coursepublic_id ")
	private Long countCoursepublicId;
	
	@Schema(description = "custom max create_date ")
	private Date maxCreateDate;

	@Schema(description = "custom true = DEGREE, show tab ")
	private Boolean courseMappingStatus;
	
}
