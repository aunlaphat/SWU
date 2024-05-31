package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sutthiyapakc
 *
 */
@Data
public class ReportOfferedCourseData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 2563456296791034777L;

	private Long depId;
	private String depNameShortTh;
	private String depNameShortEn;
	private String courseCode;
	private String courseNameTh;
	private String courseNameEn;
	private String fullCourseTh;
	private String fullCourseEn;
	private Long countRound;
	private Date dateSend;
	private String userSendTh;
	private String userSendEn;
	private Long courseStatus;
	private String statusTh;
	private String statusEn;
	
}
