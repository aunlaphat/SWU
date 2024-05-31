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
public class CourseLog extends Footprint implements Serializable {

	private static final long serialVersionUID = 7156007348235250914L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseLogId;
	
	private Long courseId;
	private Long courseMainStatus;
	
}
