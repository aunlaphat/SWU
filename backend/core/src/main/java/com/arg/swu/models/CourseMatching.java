package com.arg.swu.models;

import java.io.Serializable;

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
public class CourseMatching extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -4822426835885052858L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseMatchingId;
	
	private Long courseId;
	private Long subjectSwuId;
	private String curriculumSwuId;
	
}
