package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SimulateTranscriptCompareData implements Serializable {

    private static final long serialVersionUID = 1775621082566360510L;

    /*Request */
    @Schema(description = "fk member_info.member_id เลขประจำตัวผู้ลงทะเบียนเรียน")
    private Long memberId;

    @Schema(description = "PK รหัสหลักสูตร")
    private Long curriculumId;

   /* Response */
   @Schema(description = "ชื่อหลักสูตร (ภาษาไทย)")
   private String curriculumNameTh;    

   @Schema(description = "ชื่อหลักสูตร (ภาษาอังกฤษ)")
   private String curriculumNameEn;    
   
   @Schema(description = "ชื่อกลุ่มหมวด (ภาษาไทย)")
   private String groupTh;    

   @Schema(description = "ชื่อกลุ่มหมวด (ภาษาอังกฤษ)")
   private String groupEn;    

   @Schema(description = "รหัสวิชา (ภาษาไทย)")
   private String subjectCodeTh;    

   @Schema(description = "รหัสวิชา (ภาษาอังกฤษ)")
   private String subjectCodeEn;    

   @Schema(description = "ชื่อวิชา (ภาษาไทย)")
   private String subjectNameTh;    

   @Schema(description = "ชื่อวิชา (ภาษาอังกฤษ)")
   private String subjectNameEn;    

   @Schema(description = "จำนวนหน่วยกิต")
   private String subjectCredit;    

   @Schema(description = "หน่วยกิตที่เรียนแล้ว")
   private BigDecimal creditAmount;    

   @Schema(description = "หน่วยกิตคงเหลือ")
   private BigDecimal remain;  


}
