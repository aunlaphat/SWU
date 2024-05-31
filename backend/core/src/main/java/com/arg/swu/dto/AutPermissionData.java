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
public class AutPermissionData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = -5116128259904325543L;

	@Schema(description = "PK")
	private Long permissionId;
	
	@Schema(description = "FK (aut_role)")
	private Long roleId;
	
	@Schema(description = "FK (aut_menu)")
	private Long menuId;
	
}
