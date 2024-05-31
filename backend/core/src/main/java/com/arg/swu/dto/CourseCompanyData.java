package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class CourseCompanyData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 2170830102360052261L;
	
	@Schema(description = "PK ")
	private Long courseCompanyId;
	
	@Schema(description = "FK (course_main)" )
	private Long courseId;
	
	@Schema(description = "ชื่อหน่วยงาน/สถานประกอบการ", requiredMode = RequiredMode.REQUIRED)
	private String companyName;
	
	@Schema(description = "ที่ตั้ง", requiredMode = RequiredMode.REQUIRED)
	private String companyAddress;
	
	@Schema(description = "ชื่อผู้ประกอบการ", requiredMode = RequiredMode.REQUIRED)
	private String companyOwnerName;
	
	@Schema(description = "เบอร์โทรศัพท์", requiredMode = RequiredMode.REQUIRED)
	private String companyTel;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
