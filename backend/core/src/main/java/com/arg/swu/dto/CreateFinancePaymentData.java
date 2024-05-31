package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateFinancePaymentData implements Serializable {
	
	private static final long serialVersionUID = 2544757985212172386L;

    @Schema(description = "")
    private Long memberCourseId;
    @Schema(description = "")
    private Long paymentType;
    @Schema(description = "")
    private String couponCode;

}
