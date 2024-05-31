package com.arg.swu.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class ResponseMoodleData implements Serializable {
	
	private static final long serialVersionUID = -2445043729494498973L;
	
	private long id;
	private String username;
	private String shortname;

}
