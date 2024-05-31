/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.dto;

import com.arg.swu.models.commons.PageableCommon;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
public class MasSharePercentAttachData extends PageableCommon implements Serializable {
    private static final long serialVersionUID = 2170830102360053261L;
    
    @Schema(description = "PK auto run (share_percent_attach_id) ")
    private Long sharePercentAttachId;
    @Schema(description = "fk (depId)")
    private Long depId;
    @Schema(description = "ชื่อไฟล์", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;
    @Schema(description = "Link", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileLink;
    
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
}
