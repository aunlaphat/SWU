package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GraduateCourseSkillData implements Serializable {

    private static final long serialVersionUID = 2876531012346360988L;

    /*Request */
    @Schema(description = "fk member_info.member_id เลขประจำตัวผู้ลงทะเบียนเรียน")
    private Long memberId;

    @Schema(description = "รหัสอ้างอิง course_id")
    private Long courseId;

    /* Response */
    @Schema(description = "รหัส PK")
    private Long generalSkillId;

    @Schema(description = "รหัสทักษะทั่วไป")
    private String generalSkillCode;

    @Schema(description = "ชื่อทักษะทั่วไป (ไทย)")
    private String generalSkillNameTh;

    @Schema(description = "ชื่อทักษะทั่วไป (อังกฤษ)")
    private String generalSkillNameEn;

    @Schema(description = "ระดับทักษะ")
    private Long skillLevel;

    @Schema(description = "จำนวนชั่วโมงรวม")
    private BigDecimal sumCourseTotalH;

    @Schema(description = "levelNo")
    private String levelNo;

    @Schema(description = "levelNameTh")
    private String levelNameTh;

    @Schema(description = "levelNameEn")
    private String levelNameEn;

    @Schema(description = "skillLevelColor")
    private String skillLevelColor;
    
}
