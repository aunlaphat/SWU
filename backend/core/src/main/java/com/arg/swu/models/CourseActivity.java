package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class CourseActivity extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -2672472107791614335L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseActivityId;
	
    private Long courseId;
    private String courseActivityTopicTh;
    private String courseActivityTopicEn;
    private BigDecimal courseActivityPeriod;
    private String courseActivityMethodMore;
    private String courseActivityAssessTh;
    private String courseActivityAssessEn;

}