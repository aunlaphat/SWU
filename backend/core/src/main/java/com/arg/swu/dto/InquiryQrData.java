package com.arg.swu.dto;

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
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class InquiryQrData implements Serializable {
	
	private static final long serialVersionUID = -4326382364434023497L;
	
	private String id;
	private String object;
	private String created;
	private Boolean livemode;
	private BigDecimal amount;
	private String currency;
	private String customer;
	private String description;
	private String status;
	private Object metadata;
	
	private Charge3dSource source;
	
	@SerializedName("order_id")
	private String orderId;
	
	@SerializedName("transaction_state")
	private String transactionState;

	@SerializedName("reference_order")
	private String referenceOrder;

	@SerializedName("source_type")
	private String sourceType;

	@SerializedName("failure_code")
	private String failureCode;

	@SerializedName("failure_message")
	private String failureMessage;

	@SerializedName("expire_time_seconds")
	private Long expireTimeSeconds;
	
	@SerializedName("additional_data")
	private CheckoutAdditionalData additionalData;
	
}
