package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileData implements Serializable {

	private static final long serialVersionUID = -2866818726330554177L;
	
	private Long filesize;
	private String originalFilename;
	
//	@Schema(defaultValue = "Internal File")
	private String filename;
	private String extension;
	private String base64;
	private String prefix;
	private Long module;
	private String fullpath;
	
}
