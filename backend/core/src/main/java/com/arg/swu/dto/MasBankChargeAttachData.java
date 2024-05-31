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
public class MasBankChargeAttachData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 7148875209971175048L;

    @Schema(description = "PK")
    private Long chargeAttachId;
    
    @Schema(description = "fk อ้างอิงค่าธรรมเนียมการชำระเงิน")
    private Long chargeId;
    
    @Schema(description = "ชื่อไฟล์")
    private String fileName;
    
    @Schema(description = "Link")
    private String fileLink;
    
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;


}
