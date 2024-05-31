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
public class MemberAddressData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -7983277539489983897L;

	@Schema(description = "PK")
	private Long memberAddressId;
	
	@Schema(description = "Fk (member_info)")
	private Long memberId;
	
	@Schema(description = "ประเภทที่อยู่ 30031000")
	private Long addressType;
	
	@Schema(description = "ที่อยู่")
	private String addressDetail;
	
	@Schema(description = "จังหวัด (sys_province)")
	private Long addressProvince;
	
	@Schema(description = "อำเภอ (sys_amphur)")
	private Long addressAmphur;
	
	@Schema(description = "ตำบล (sys_tambon)")
	private Long addressTambon;
	
	@Schema(description = "รหัสไปรษณีย์")
	private Long addressZipcode;
	
}
