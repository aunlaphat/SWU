package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GraduateCourseData implements Serializable {

    private static final long serialVersionUID = 2997731096722360631L;

    /*Request */
    @Schema(description = "fk member_info.member_id เลขประจำตัวผู้ลงทะเบียนเรียน")
    private Long memberId;

    @Schema(description = "start Date")
    private Date startDate;

    @Schema(description = "end Date")
    private Date endDate;

    /* Response */
    @Schema(description = "รหัสอ้างอิง course_id")
    private Long courseId;

    @Schema(description = "รหัสหลักสูตร")
    private String courseCode;
    
    @Schema(description = "ชื่อหลักสูตร (ภาษาไทย)")
    private String courseNameTh;

    @Schema(description = "ชื่อหลักสูตร (ภาษาอังกฤษ)")
    private String courseNameEn;

}
