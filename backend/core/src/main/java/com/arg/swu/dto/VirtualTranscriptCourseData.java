package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VirtualTranscriptCourseData implements Serializable {

    private static final long serialVersionUID = 1775621082566360510L;

    @Schema(description = "PK หลักสูตร")
    private String courseId;

    @Schema(description = "รหัสหลักสูตร")
    private String courseCode;

    @Schema(description = "ชื่อหลักสูตร (ภาษาไทย)")
    private String courseNameTh;

    @Schema(description = "ชื่อหลักสูตร (ภาษาอังกฤษ)")
    private String courseNameEn;

    @Schema(description = "รหัสวิชา (ภาษาไทย)")
    private String subjectCodeTh;

    @Schema(description = "รหัสวิชา (ภาษาอังกฤษ)")
    private String subjectCodeEn;

    @Schema(description = "ชื่อวิชา (ภาษาไทย)")
    private String subjectNameTh;

    @Schema(description = "ชื่อวิชา (ภาษาอังกฤษ)")
    private String subjectNameEn;

    @Schema(description = "วันที่เรียนเริ่มต้น")
    private Date courseClassStart;

    @Schema(description = "วันที่เรียนสิ้นสุด")
    private Date courseClassEnd;

    @Schema(description = "จำนวนหน่วยกิต")
    private Long creditAmount;

    @Schema(description = "เกรด")
    private String resultGrade;

    @Schema(description = "ผลลัพธ์การเรียนรู้ที่คาดหวัง")
    private String courseScloDesc;

    @Schema(description = "ทักษะที่ได้รับภาษาไทย")
    private String skillNameTh;

    @Schema(description = "ทักษะที่ได้รับภาษาอังกฤษ")
    private String skillNameEn;

    @Schema(description = "จำนวนชั่วโมงการเรียนทฤษฏี")
    private BigDecimal courseTheoryH;
    
    @Schema(description = "จำนวนชั่วโมงการเรียนปฏิบัติ")
    private BigDecimal courseActionH;

}
