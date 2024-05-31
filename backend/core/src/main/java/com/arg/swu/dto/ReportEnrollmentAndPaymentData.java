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
public class ReportEnrollmentAndPaymentData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -1056131132087229962L;
	
	private Long depId;
	private String depNameShortTh;
	private String depNameShortEn;
	private String courseCode;
	private String publicNameTh;
	private String publicNameEn;
	private String fullCourseTh;
	private String fullCourseEn;
	private Long registerCount;
	private BigDecimal remainAmount;
	
}
