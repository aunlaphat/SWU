package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class KPaymentData implements Serializable {

	private static final long serialVersionUID = 2124945724798070382L;
	
	private String src;
	private String apiKey;
	private BigDecimal amount;
	private String currency;
	private String paymentMethod;
	private String name;
	private String mid;
	private String tid;
	private String orderId;
	
	@Schema(description = "custom referenceOrder")
	private String referenceOrder;
	
}
