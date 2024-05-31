package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sutthiyapakc
 *
 */
@Data
public class FinanceImportLogData extends PageableCommon implements Serializable {

    private static final long serialVersionUID = 5666272900479554829L;

	@Schema(description = "PK")
	private Long impId;
	
	@Schema(description = "fileReferenceCode")
    private String fileReferenceCode;

	@Schema(description = "impFileName")
	private String impFileName;
    
	@Schema(description = "impFileLink")
	private String impFileLink;
	
	@Schema(description = "impFileSize")
	private Long impFileSize;
	
	@Schema(description = "impFileRow")
	private Long impFileRow;
	
	@Schema(description = "passRow")
	private Long passRow;
    
	@Schema(description = "failRow")
	private Long failRow;

	@Schema(description = "missRow")
	private Long missRow;

	@Schema(description = "messageError")
	private String messageError;

	@Schema(description = "impFileMoney")
	private BigDecimal impFileMoney;

	@Schema(description = "coursepublicId")
	private Long coursepublicId;
	
	@Schema(description = "custom วันที่นำเข้า")
	private Date createDate;
	
	@Schema(description = "custom courseCode")
	private String courseCode;
	
	@Schema(description = "custom publicNameTh")
	private String publicNameTh;
	
	@Schema(description = "custom publicNameEn")
	private String publicNameEn;
	
	@Schema(description = "custom วันที่ชำระเงินใช้ในการค้นหา")
	private List<Date> createDateList;
	
	@Schema(description = "custom prcessing, imported, generated, success")
	private String status;
	
	@Schema(description = "Output file zip cert, receipt")
    private String resultFileZip;
    
}
