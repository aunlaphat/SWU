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
public class CourseInstructor extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -5300643333851933867L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseInstructorId;
	
	private Long courseId;
	private Long instructorId;
	private Boolean instructorMain;
	private Boolean instructorType;
	private String externalNameTh;
	private String externalNameEn;
	private String externalEmail;
	private String externalPhotoPath;
	private String filename;
	private String prefix;
	private Long module;
	
}
