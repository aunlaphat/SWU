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
public class ReportCanceledCourseData extends PageableCommon implements Serializable {
	

	private static final long serialVersionUID = 7929198800242505191L;

	private Long coursepublicId;
	private String courseCode;
	private String publicNameTh;
	private String publicNameEn;
	private String fullCourseTh;
	private String fullCourseEn;
	private Date requestCancelDate;
	private Long memberCount;
	private Long paymentCount;
	private Long coursepublicStatus;
	private String coursepublicStatusTh;
	private String coursepublicStatusEn;
	
	private List<Date> requestCancelDateList;
	private Long courseGeneration;
	private BigDecimal remainAmount;
	private Long registerCount;
	
}
