package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FinancePaymentPatchData implements Serializable {
	

	private static final long serialVersionUID = 5126752542542175255L;
	
	@Schema(description = "paymentId")
	private Long paymentId;

	@Schema(description = "memberReceiptViewFlag")
	private Boolean memberReceiptViewFlag;
	
}
