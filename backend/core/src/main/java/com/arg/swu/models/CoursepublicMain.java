package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

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
public class CoursepublicMain extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -7662788234137848866L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coursepublicId;
	
	private Long courseId;
	private Long coursepublicStatus;
	private Date courseRegisStart;
	private Date courseRegisEnd;
	private Long maxEnroll;
	private Long feeStatus;
	private BigDecimal feeAmount;
	private BigDecimal promotionAmount;
	private Long courseGeneration;
	private String publicNameTh;
	private String publicNameEn;
	private Long depIdLevel1;
	private Long depIdLevel2;
	private Long courseFormat;
	private String courseFormatDescTh;
	private String courseFormatDescEn;
	private Boolean buasriStatus;
	private Boolean popularStatus;
	private Long[] certificateFormat;
	private Long publicFormat;
	private Boolean publicStatus;
	private Date coursePublicStart;
	private Date coursePublicEnd;
	private Date courseClassStart;
	private Date courseClassEnd;
	private String courseTimeTh;
	private String courseTimeEn;
	private Date promotionStart;
	private Date promotionEnd;
	private BigDecimal costShareDepPercent;
	private BigDecimal costShareGlobalPercent;
	private BigDecimal costShareCenterPercent;
	private Long totalRegister;
        private Date requestCancelDate;

	private Long moodleCourseId;
}
