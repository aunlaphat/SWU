package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CourseMain extends Footprint implements Serializable {

	private static final long serialVersionUID = 3593885492997300652L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseId;
	
	private Long courseRefId;
	private Long courseVersion;
	private Long courseNewStatus;
	private Long courseTypeId;
	private Long depIdLevel1;
	private Long depIdLevel2;
	private String courseCode;
	private String courseNameTh;
	private String courseNameEn;
	private String courseDescTh;
	private String courseDescEn;
	private String[] courseHashtag;
	private Long courseMainStatus;
	private Boolean forceStatus;
	private Long courseFormat;
	private String courseFormatDescTh;
	private String courseFormatDescEn;
	
	@Column(name = "course_theory_h")
	private BigDecimal courseTheoryH;

	@Column(name = "course_action_h")
	private BigDecimal courseActionH;

	@Column(name = "course_total_h")
	private BigDecimal courseTotalH;
	
	private BigDecimal courseDurationTime;
	private Long durationTimeUnit;
	private Long gradeFormat;
	private BigDecimal creditAmount;
	private Long industryGroupId;
	private Boolean targetGroupOtherStatus;
	private String targetGroupOtherName;
	private String courseSpecificRequirementTh;
	private String courseSpecificRequirementEn;
	private Date requestDate;
	
}
