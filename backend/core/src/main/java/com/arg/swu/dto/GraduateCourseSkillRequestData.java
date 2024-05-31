package com.arg.swu.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GraduateCourseSkillRequestData implements Serializable {

    private static final long serialVersionUID = 2224531012245260999L;

    /*Request */
    @Schema(description = "fk member_info.member_id เลขประจำตัวผู้ลงทะเบียนเรียน")
    private Long memberId;

    @Schema(description = "List รหัสอ้างอิง course_id")
    private List<Long> courseIds;

}
