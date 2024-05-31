package com.arg.swu.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class DropdownCriteriaData implements Serializable {

	private static final long serialVersionUID = -3457754645519548125L;
	
	@Schema(description = "true or 1 เพื่อแสดง code ใน dropdown ")
	private Boolean displayCode;
	
	@Schema(description = "id เพื่อใช้ในการกรอง กรณีมีเป็น tree ")
	private Long id;

	@Schema(description = "ใข้สำหรับ department ")
	private Long depType;
	
	@Schema(description = "id รองเพื่อใช้ในการกรอง กรณีมีเป็น tree ")
	private Long childId;
	
	@Schema(description = "custom mode ")
	private String mode;

	@Schema(description = "PK id เพื่อใช้ในการค้นหา ")
	private Long pkId;
	
	@Schema(description = "PK ids เพื่อใช้ในการค้นหา ")
	private List<Long> pkIds;

	@Schema(description = "คำที่ใช้ค้นหา")
	private String searchValue;
	
}
