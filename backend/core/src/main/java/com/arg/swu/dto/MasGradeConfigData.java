package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class MasGradeConfigData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 3481542812396167674L;
	
	@Schema(description = "PK")
	private Long gradeId;
	
	@Schema(description = "Grade Symbol")
	private String gradeSymbol;
	
	@Schema(description = "Grade No")
	private Long gradeNo;
	
	@Schema(description = "Grade Format")
	private Long gradeFormat;
	
        @Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
                
        @Schema(description = "Create Date",requiredMode = RequiredMode.REQUIRED)
	private Date createDate;
        
        @Schema(description = "Create By Id",requiredMode = RequiredMode.REQUIRED)
	private Long createById;
        
        @Schema(description = "Update Date")
	private Date updateDate;
        
        @Schema(description = "Update By Id")
	private Long updateById;
}
