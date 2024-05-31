package com.arg.swu.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class AllMenuData implements Serializable {

	private static final long serialVersionUID = -3645551900798330416L;

	private Long permissionId;
	
	private Long menuId;
	private String menuCode;
	private String menuType;
	private Long parentId;
	private Long orderNumber;
	private String name;
	private String showNameKey;
	private String link;
	private Boolean activeFlag;
	
}
