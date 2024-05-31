package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class CoursepublicMainData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 3978024348719561345L;

	@Schema(description = "PK")
	private Long coursepublicId;

	@Schema(description = "FK (course_main)")
	private Long courseId;

	@Schema(description = "สถานะเปิดรอบหลักสูตร 30014000")
	private Long coursepublicStatus;

	@Schema(description = "วันที่เปิดรับสมัคร")
	private Date courseRegisStart;

	@Schema(description = "วันที่ปิดรับสมัคร")
	private Date courseRegisEnd;

	@Schema(description = "จำนวนที่เปิดรับ")
	private Long maxEnroll;

	@Schema(description = "การคิดค่าลงทะเบียน 30008000")
	private Long feeStatus;

	@Schema(description = "ค่าลงทะเบียน / ราคา")
	private BigDecimal feeAmount;

	@Schema(description = "ราคาโปรโมชั่น")
	private BigDecimal promotionAmount;

	@Schema(description = "รุ่นที่")
	private Long courseGeneration;

	@Schema(description = "ชื่อรอบหลักสูตร (ภาษาไทย)")
	private String publicNameTh;

	@Schema(description = "ชื่อรอบหลักสูตร (ภาษาอังกฤษ)")
	private String publicNameEn;

	@Schema(description = "คณะ/หน่วยงาน")
	private Long depIdLevel1;

	@Schema(description = "ภาควิชา")
	private Long depIdLevel2;

	@Schema(description = "รูปแบบการเรียนการสอน")
	private Long courseFormat;

	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาไทย)")
	private String courseFormatDescTh;

	@Schema(description = "รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาอังกฤษ)")
	private String courseFormatDescEn;

	@Schema(description = "ให้บริการบัวศรีไอดี")
	private Boolean buasriStatus;

	@Schema(description = "Popular")
	private Boolean popularStatus;

	@Schema(description = "รูปแบบการให้ประกาศนียบัตร 30006000")
	private Long[] certificateFormat;

	@Schema(description = "รูปแบบการเปิดหลักสูตร 30007000")
	private Long publicFormat;

	@Schema(description = "เผยแพร่บนเว็บไซต์")
	private Boolean publicStatus;

	@Schema(description = "วันที่โปรโมทเริ่มต้น")
	private Date coursePublicStart;

	@Schema(description = "วันที่โปรโมทสิ้นสุด")
	private Date coursePublicEnd;

	@Schema(description = "วันที่เรียนเริ่มต้น")
	private Date courseClassStart;

	@Schema(description = "วันที่เรียนสิ้นสุด")
	private Date courseClassEnd;

	@Schema(description = "เวลาเรียน (ภาษาไทย)")
	private String courseTimeTh;

	@Schema(description = "เวลาเรียน (ภาษาอังกฤษ)")
	private String courseTimeEn;

	@Schema(description = " วันที่เริ่มจัดโปรโมชั่นส่วนลด")
	private Date promotionStart;

	@Schema(description = "วันสุดท้ายที่จัดโปรโมชั่นส่วนลด")
	private Date promotionEnd;

	@Schema(description = "ส่วนแบ่งคณะ")
	private BigDecimal costShareDepPercent;

	@Schema(description = "ส่วนแบ่งมหาวิทยาลัย")
	private BigDecimal costShareGlobalPercent;

	@Schema(description = "ส่วนแบ่งศูนย์บริการวิชาการ")
	private BigDecimal costShareCenterPercent;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "custom ชื่อ Thumbnail (ภาษาไทย) (coursepublic_media)")
	private String mediaNameTh;
	
	@Schema(description = "custom ชื่อ Thumbnail (ภาษาอังกฤษ) (coursepublic_media)")
	private String mediaNameEn;
	
	@Schema(description = "custom (coursepublic_media) filename")
	private String filename;
	
	@Schema(description = "custom (coursepublic_media) Prefix")
	private String prefix;
	
	@Schema(description = "custom (coursepublic_media) Module")
	private Long module;
	
	@Schema(description = "custom รหัสหลักสูตร")
	private String courseCode;
        
    @Schema(description = "Count Actived")
    private Long countActived;
        
    @Schema(description = "course type name local")
    private String nameLo;
        
    @Schema(description = "course type name en")
    private String nameEn;
        
    @Schema(description = "grade format")
    private Long gradeFormat;
        
    @Schema(description = "Custom financeImportLog")
    private FinanceImportLogData financeImportLog;

    @Schema(description = "custom สถานะเปิดรอบหลักสูตร (ภาษาอังกฤษ)")
    private String coursepublicStatusEn;
    
    @Schema(description = "custom สถานะเปิดรอบหลักสูตร (ภาษาไทย)")
    private String coursepublicStatusTh;

    @Schema(description = "custom thumbnail")
    private String thumbnail;

    @Schema(description = "moodle_course_id")
    private Long moodleCourseId;
    
    private String cancelReason;
    private Date requestCancelDate;
    private Date createDate;
    private Long createById;
    private Date passDate; 
	
}
