package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class CheckoutCardData implements Serializable {
	
	private static final long serialVersionUID = 4619636767091982003L;

	private BigDecimal amount;
	private String currency;
	private String description;
	private String sourceType;
	private String mode;
	private String referenceOrder;
	private String token;
	private String savecard;
	private String orderId;
	private String ref1;
	private String ref2;
	private String ref3;
	
}
