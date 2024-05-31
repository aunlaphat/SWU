/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.dto;

import com.arg.swu.models.commons.PageableCommon;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
public class MasSharePercentHistoryData extends PageableCommon implements Serializable{
    private static final long serialVersionUID = 2170830102360053261L;
    
    @Schema(description = "PK auto run (share_percent_history_id) ")
    private Long sharePercentHistoryId;
    @Schema(description = "fk (dep_id)")
    private Long depId;
    @Schema(description = "ส่วนแบ่งคณะ")
    private BigDecimal costShareDepPercent;
    @Schema(description = "ส่วนแบ่งมหาลัย")
    private BigDecimal costShareGlobalPercent;
    @Schema(description = "ส่วนแบ่งศูนย์บริการวิชาการ")
    private BigDecimal costShareCenterPercent;
    @Schema(description = "ผู้บันทึก")
    private String firstName;
    @Schema(description = "วันที่บันทึก")
    private String createDate;
    @Schema(description = "สร้างโดย")
    private Long createByUserId;
    
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;

    @Schema(description = "custom ผู้บันทึก (ภาษาไทย)")
    private String fullNameTh;

    @Schema(description = "custom ผู้บันทึก (ภาษาอังกฤษ)")
    private String fullNameEn;

	
}
