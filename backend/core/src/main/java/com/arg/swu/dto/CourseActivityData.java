package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CourseActivityData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = -1276157166626056125L;

	@Schema(description = "PK")
	private Long courseActivityId;
	
	@Schema(description = "FK (course_main)")
	private Long courseId;
	
	@Schema(description = "เนื้อหา/กระบวนการ (ไทย)")
	private String courseActivityTopicTh;
	
	@Schema(description = "เนื้อหา/กระบวนการ (อังกฤษ)")
	private String courseActivityTopicEn;
	
	@Schema(description = "ระยะเวลา (ชั่วโมง)")
    private BigDecimal courseActivityPeriod;
	
	@Schema(description = "รายละเอียดกิจกรรมการเรียนการสอนเพิ่มเติม")
    private String courseActivityMethodMore;
	
	@Schema(description = "การวัดและประเมินผล (ไทย)")
    private String courseActivityAssessTh;
	
	@Schema(description = "การวัดและประเมินผล (อังกฤษ)")
    private String courseActivityAssessEn;

	@Schema(description = "custom วิธีการสอน (ภาษาไทย)")
	private String coruseActivityMethodTh;
	
	@Schema(description = "custom วิธีการสอน (ภาษาอังกฤษ)")
	private String coruseActivityMethodEn;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;

	@Schema(description = "custom วิธีการสอน Fk(course_activity_method)")
	private List<CourseActivityMethodData> courseActiviryMethodList;
	
}
