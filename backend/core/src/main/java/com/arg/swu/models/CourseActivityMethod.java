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
public class CourseActivityMethod extends Footprint implements Serializable {

	private static final long serialVersionUID = 8141275620147874199L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseActivityMethodId;

	private Long courseId;
	private Long courseActivityId;
	private Long courseMethodId;
	private String courseMethodDetail;
	
}
