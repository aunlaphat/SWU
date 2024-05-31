package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 
 * @author natthawutl
 *
 */
@Data
public class MasGeneralSkillLevelData implements Serializable{

	private static final long serialVersionUID = -7861695221724882563L;
	
	@Schema(description = "pk Skill Level Id")
	private Long skillLevelId;
	
	@Schema(description = "ไอดีหมวดหมู่ทักษะ")
	private Long generalSkillId;
	
	@Schema(description = "เลเวล")
	private Long levelNo;
	
	@Schema(description = "คำอธิบาย (ภาษาไทย)")
	private String descTh;
	
	@Schema(description = "คำอธิบาย (ภาษาอังกฤษ)")
	private String descEn;
	
	@Schema(description = "วิธีการประเมิณเชิงประจักษ์")
	private String evaluationEvident;
	
	@Schema(description = "เกณฑ์ที่ใช้ในการประเมิณ")
	private String evaluationCriteria;
	
	@Schema(description = "สถานะ")
	private Boolean activeFlag;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
}
