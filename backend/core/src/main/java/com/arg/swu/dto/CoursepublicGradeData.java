package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.PageableCommon;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CoursepublicGradeData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -7662788234137848866L;
	
	@Schema(description = "PK")
	private Long coursepublicGradeId;
	
    @Schema(description = "Grade symbol")
	private String gradeSymbol;
    
    @Schema(description = "Score min")
	private BigDecimal scoreMin;
    
    @Schema(description = "Score max")
	private BigDecimal scoreMax;
    
    @Schema(description = "Score")
	private BigDecimal score;
    
    @Schema(description = "Pass status")
	private Boolean passStatus;
    
    @Schema(description = "Course public id")
    private Long coursepublicId;
    
    @Schema(description = "member id")
    private Long memberId;
    private Long createById;
    private Date createDate;
}
