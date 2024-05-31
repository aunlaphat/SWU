package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MasFeeConfigData implements Serializable {

    private static final long serialVersionUID = 163036899272003518L;

    @Schema(description = "รหัส PK")
    private Long feeId;

    @Schema(description = "รหัสค่าธรรมเนียม")
    private String feeCode;

    @Schema(description = "ชื่อค่าธรรมเนียม (ไทย)")
    private String feeNameTh;

    @Schema(description = "ชื่อค่าธรรมเนียม (อังกฤษ)")
    private String feeNameEn;

    @Schema(description = "For Calculate (1,-1) ")
    private String feeFormatCal;

    @Schema(description = "สำหรับเรียงลำดับความสำคัญรายการ")
    private String feeOrder;
    
    @Schema(description = "fee Amount")
    private BigDecimal feeAmount;

    @Schema(description = "ref Table")
    private String refTable;

    @Schema(description = "ref Id")
    private Long refId;

}
