package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class SysTambonData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 9182146691883636506L;

	private Long tambonId;
	private Long amphurId;
	private String amphurCode;
	private String tambonCode;
	private String tambonNameTh;
	private String tambonNameEn;
	private String zipCode;
	
}