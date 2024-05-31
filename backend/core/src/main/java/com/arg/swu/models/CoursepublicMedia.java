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
public class CoursepublicMedia extends Footprint implements Serializable {

	private static final long serialVersionUID = -697846691963856516L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coursepublicMediaId;
	
	private Long coursepublicId;
	private Long mediaType;
	private String mediaNameTh;
	private String mediaNameEn;
	private String prefix;
	private Long module;
	private String filename;

}
