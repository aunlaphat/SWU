package com.arg.swu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class NotifLecturerCourseRegister {
    private String instructorNameTh;
    private String instructorNameEn;
    private String studentNameEn;
    private String studentNameTh;
    private Long memberId;
    private String learneerId;
    private String publicNameTh;
    private String publicNameEn;
    private String courseCode;
    private String email;
    
}
