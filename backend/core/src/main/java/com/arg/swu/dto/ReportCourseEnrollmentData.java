package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sutthiyapakc
 *
 */
@Data
public class ReportCourseEnrollmentData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 7701371948644235011L;
	
	
	private String courseCode;
	private String publicNameTh;
	private String publicNameEn;
	private String fullCourseTh;
	private String fullCourseEn;
	private Long maxEnroll;
	private Long registerAmount;
	private Long completedAmount;
	private Long courseStatus;
	private String statusTh;
	private String statusEn;
	
	
}
