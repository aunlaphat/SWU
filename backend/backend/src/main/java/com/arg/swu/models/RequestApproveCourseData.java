package com.arg.swu.models;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
public class RequestApproveCourseData {
    private Long courseId;
    private String professor;
    private String professorEn;
    private String professorEmail;
    private String courseCode;
    private String courseNameTh;
    private String courseNameEn;
    private Date createDate;    
    private String courseTypeCode;
    private String courseTypeNameTh;
    private String courseTypeNameEn;
    private String teachType;
    private String courseCompareTh;
    private String courseCompareEn;
    private String createById;
    private String refUserType;
    private String userId;
    private String staffEmail;
    private String staffName;
    private String staffNameEn;

    private Date courseRegisStart;
    private Date dateRequire;
}
