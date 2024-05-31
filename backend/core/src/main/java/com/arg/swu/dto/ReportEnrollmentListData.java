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
public class ReportEnrollmentListData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 2747808017443559328L;

	private Long memberCourseId;
	private String courseCode;
	private String publicNameTh;
	private String publicNameEn;
	private String fullCourseTh;
	private String fullCourseEn;
	private Long memberId;
	private String prefixnameNameTh;
	private String prefixnameNameEn;
	private String memberFirstnameTh;
	private String memberFirstnameEn;
	private String memberLastnameTh;
	private String memberLastnameEn;
	private String fullNameTh;
	private String fullNameEn;
	private String receiptNo;
	private Date receiptDate;
	private Long payType;
	private String payTypeTh;
	private String payTypeEn;
	private BigDecimal paymentAmount;
	private Date transactionDatetime;
	private Long studyStatus;
	private String studyStatusTh;
	private String studyStatusEn;
	
	private List<Date> receiptDateList;
	private List<Date> transactionDatetimeList;
	
}
