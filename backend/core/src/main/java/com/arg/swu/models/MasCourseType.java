package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
public class MasCourseType  extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 3865471099475067738L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseTypeId;

    private String courseTypeCode;
    private String courseTypeNameTh;
    private String courseTypeNameEn;
    private Boolean courseMappingStatus;
    private Long courseTypeOrder;
    private String filename;
    private String prefix;
    private Long module;

}
