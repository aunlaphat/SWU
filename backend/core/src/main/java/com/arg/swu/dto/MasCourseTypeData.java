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
public class MasCourseTypeData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 4584208362782253889L;

    @Schema(description = "ประเภทหลักสูตร")
    private Long courseTypeId;

    @Schema(description = "รหัสประเภทหลักสูตร")
    private String courseTypeCode;

    @Schema(description = "ชื่อประเภทหลักสูตร (ภาษาไทย)")
    private String courseTypeNameTh;

    @Schema(description = "ชื่อประเภทหลักสูตร (อังกฤษ)")
    private String courseTypeNameEn;

    @Schema(description = "การเทียบเคียงหลักสูตร")
    private Boolean courseMappingStatus;

    @Schema(description = "เรียงลำดับ")
    private Long courseTypeOrder;
    
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	private String filename;
    private String prefix;
    private Long module;


}
