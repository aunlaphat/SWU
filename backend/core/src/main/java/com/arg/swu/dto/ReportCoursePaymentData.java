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
public class ReportCoursePaymentData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -2692268555748665027L;
	
	private String courseCode;
	private String publicNameTh;
	private String publicNameEn;
	private String fullCourseTh;
	private String fullCourseEn;
	private Long registerCount;
	private BigDecimal registerAmount;
	private BigDecimal payAmount;
	private BigDecimal refundAmount;
	private BigDecimal remainAmount;
	
}
