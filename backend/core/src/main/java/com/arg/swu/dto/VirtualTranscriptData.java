package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VirtualTranscriptData implements Serializable {

    private static final long serialVersionUID = 1886731085866360620L;

    /*Request */
    @Schema(description = "fk member_info.member_id เลขประจำตัวผู้ลงทะเบียนเรียน")
    private Long memberId;

    @Schema(description = "member_course.pass_date startDate")
    private Date startDate;

    @Schema(description = "member_course.pass_date endDate")
    private Date endDate;
    
   /* Response Header */
    @Schema(description = "คำนำหน้า ภาษาไทย")
    private String prefixnameNameTh;

    @Schema(description = "คำนำหน้า ภาษาอังกฤษ")
    private String prefixnameNameEn;

    @Schema(description = "ชื่อ (ไทย)")
    private String memberFirstnameTh;

    @Schema(description = "นามสกุล (ไทย)")
    private String memberLastnameTh;

    @Schema(description = "ชื่อ (อังกฤษ)")
    private String memberFirstnameEn;

    @Schema(description = "นามสกุล (อังกฤษ)")
    private String memberLastnameEn;

    @Schema(description = "อีเมล")
    private String memberEmail;

    /*Response Footer */
    @Schema(description = "Token สำหรับ verify เอกสาร")
    private String virtualTranscriptVerify;

    @Schema(description = "Token สำหรับ verify เอกสาร")
    private String learningOutcomesVerify;
    
    @Schema(description = "Token สำหรับ verify เอกสาร")
    private String experienceTranscriptVerify;

    @Schema(description = "จำนวนคอร์ส degree ที่จบแล้ว")
    private Long degreeCourseTaken;

    @Schema(description = "เทียบเคียงรายวิชา > นับจำนวนรายวิชา")
    private BigDecimal countSubjectCode;

    @Schema(description = "สะสมหน่วยกิต")
    private BigDecimal creditBank;

    @Schema(description = "virtual Transcript Sign Path")
    private String virtualTranscriptSignPath;  

    @Schema(description = "Path สำหรับ เก็บไฟล์ tran ที่ gen")
    private String virtualTranscriptPdfPath;  
    
    @Schema(description = "Path สำหรับ เก็บไฟล์ outcome ที่ gen")
    private String learningOutcomesPdfPath;  

    @Schema(description = "Path สำหรับ เก็บไฟล์ experience tran ที่ gen")
    private String experienceTranscriptPdfPath;  

    @Schema(description = "Course")
	List<VirtualTranscriptCourseData> virtualTranscriptCourses;

    @Schema(description = "certType")
    private String certType;

}
