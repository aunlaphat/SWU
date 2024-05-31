package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SimulateTranscriptData implements Serializable {

    private static final long serialVersionUID = 2997731096722360631L;

    @Schema(description = "")
    private Long simulateTranscriptId;

    /*Request */
    @Schema(description = "fk member_info.member_id เลขประจำตัวผู้ลงทะเบียนเรียน")
    private Long memberId;

    @Schema(description = "รหัสอ้างอิงคอร์ส")
    private Long courseId;

    @Schema(description = "ผลการเรียน")
    private String resultGrade;

    /* Response */
    @Schema(description = "รหัสคอร์ส")
    private String courseCode;

    @Schema(description = "ชื่อคอร์ส (ไทย)")
    private String courseNameTh;
    
    @Schema(description = "ชื่อคอร์ส (อังกฤษ)")
    private String courseNameEn;
    
    @Schema(description = "จำนวนหน่วยกิต")
    private BigDecimal creditAmount;
    
    @Schema(description = "รูปแบบหน่วยกิต")
    private String creditType;
    
}
