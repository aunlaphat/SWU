package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class TmpFinanceImportLogData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -1687610661871354369L;

	@Schema(description = "PK")
	private Long tmpImpId;
	
	@Schema(description = "uuid")
	private String uuid;
	
	@Schema(description = "Ref นำเข้า")
	private String fileReferenceCode;
	
	@Schema(description = "ชื่อไฟล์")
	private String impFileName;
	
	@Schema(description = "ลิงค์ไฟล์นำเข้า")
	private String impFileLink;
	
	@Schema(description = "ขนาดไฟล์นำเข้า")
	private Long impFileSize;
	
	@Schema(description = "จำนวนทั้งหมดตามไฟล์")
	private Long impFileRow;
	
	@Schema(description = "จำนวนรายการ")
	private Long passRow;
	
	@Schema(description = "จำนวนที่ไม่ถุกต้อง")
	private Long failRow;
	
	@Schema(description = "จำนวนที่่ไม่พบข้อมูล")
	private Long missRow;
	
	@Schema(description = "ข้อความ Error")
	private String messageError;
	
	@Schema(description = "ยอดเงินรวม")
	private BigDecimal impFileMoney;
	
	@Schema(description = "Fk (coursepubic_main)")
	private Long coursepublicId;
	
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "Custom หลักสูตร")
	private CoursepublicMainData coursepublicMainData;
	
	@Schema(description = "Prefix")
	private String prefix;
	
	@Schema(description = "Module")
	private Long module;
	
}
