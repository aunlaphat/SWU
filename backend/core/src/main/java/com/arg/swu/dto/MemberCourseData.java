/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.dto;

import com.arg.swu.models.commons.PageableCommon;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
public class MemberCourseData extends PageableCommon implements Serializable{
	
    private static final long serialVersionUID = 3254125805984268372L;
    
	@Schema(description = "member_course_id(PK)")
    private Long memberCourseId; 
    @Schema(description = "coursepublic_id")
    private Long coursepublicId;
    @Schema(description = "member_id")
    private Long memberId;
    @Schema(description = "member_photo_path")
    private String filename;
    @Schema(description = "Prefix")
    private String prefix;
    @Schema(description = "Module")
    private Long module;
    @Schema(description = "prefixname_code")
    private String prefixnameCode;
    @Schema(description = "member_firstname_th")
    private String memberFirstnameTh;
    @Schema(description = "member_lastname_th")
    private String memberLastnameTh;
    @Schema(description = "member_firstname_en")
    private String memberFirstnameEn;
    @Schema(description = "member_lastname_en")
    private String memberLastnameEn;
    @Schema(description = "register_date")
    private Date registerDate;
    @Schema(description = "payment_status")
    private Long paymentStatus;
    @Schema(description = "study_status")
    private Long studyStatus;
    @Schema(description = "paymentNameLo")
    private String paymentNameLo;
    @Schema(description = "paymentNameEn")
    private String paymentNameEn;
    @Schema(description = "studyStatusNameLo")
    private String studyStatusNameLo;
    @Schema(description = "studyStatusNameEn")
    private String studyStatusNameEn;
    @Schema(description = "prefixname_name_th")
    private String prefixnameNameTh;
    @Schema(description = "prefixname_name_en")
    private String prefixnameNameEn;
    @Schema(description = "result_score")
    private BigDecimal resultScore;
    @Schema(description = "result_grade")
    private String resultGrade;
    @Schema(description = "pass_status")
    private Boolean passStatus;
    @Schema(description = "create_date")
    private Date createDate;
    @Schema(description = "update_date")
    private Date updateDate;
    @Schema(description = "create_by_id")
    private Long createById;
    @Schema(description = "update_by_id")
    private Long updateById;
    @Schema(description = "active_flag")
    private Boolean activeFlag;
    private Long memberCount;
    @Schema(description = "pass_date")
    private Date passDate;
    private String certificateNo;
    //criteria for search only
    private String nameOrSurnameTh;
    private String nameOrSurnameEn;
    private String courseCode;
    private Long courseId;
    private String memberNo;
    private Long moodleUserId;
}
