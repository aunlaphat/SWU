package com.arg.swu.dto;

import java.io.Serializable;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class AutRoleData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = -999196072562226850L;

	@Schema(description = "PK")
	private Long roleId;
	
	@Schema(description = "ชื่อสิทธิ์")
	private String name;
	
	@Schema(description = "การอนุญาต")
	private List<AllMenuData> allMenu;
	
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
