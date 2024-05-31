package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.arg.swu.models.commons.Footprint;
import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
*
* @author kumpeep
*/
@Data
public class MasWebsiteBannerData extends PageableCommon implements Serializable{
	
	private static final long serialVersionUID = 5970856138072341720L;
	
	@Schema(description = "PK banner_id ")
    private Long bannerId;
	@Schema(description = "ชื่อแบนเนอร์")
	private String bannerName;
	@Schema(description = "ลิงก์แนบแบนเนอร์")
	private String bannerLink;
	@Schema(description = "วันที่เรื่มแสดง")
	private Date displayStartDate;
	@Schema(description = "วันสิ้นสุดการแสดง")
	private Date displayEndDate;
	@Schema(description = "รูปแบนเนอร์ (ภาษาไทย)")
	private String bannerImageTh;
	@Schema(description = "รูปแบนเนอร์ (ภาษาอังกฤษ)")
	private String bannerImageEn;
	@Schema(description = "ลำดับแบนเนอร์")
	private Long orderBy;
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	@Schema(description = "วันที่อัปเดต")
	private Date updateDate;
	@Schema(description = "Prefix")
	private String prefix;
	  
	@Schema(description = "Module")
	private Long module;
	  
	@Schema(description = "ชื่อไฟล์")
	private String filename;
	@Schema(description = "custom รูปแบนเนอร์")
	private String base64;
	
	private List<Date> lastUpDateList;
	
	private String fullNameTh;
	private String fullNameEn;
	

}
