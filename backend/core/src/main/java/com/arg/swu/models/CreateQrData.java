package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

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
@Builder(toBuilder =  true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateQrData implements Serializable {

	private static final long serialVersionUID = 6921950270889061799L;
	
	private BigDecimal amount;
	private String currency;
	private String description;
	
	@SerializedName("source_type")
	private String sourceType;
	
	@SerializedName("reference_order")
	private String referenceOrder;
	
}
