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
public class Charge3dCallbackReq implements Serializable {
	
	private static final long serialVersionUID = -119797713799530406L;
	
	private String id;
	
	@SerializedName("order_id")
	private String orderId;
	private String object;
	private BigDecimal amount;
	private String currency;
	
	@SerializedName("transaction_state")
	private String transactionState;
	private String created;
	private String status;
	
	@SerializedName("reference_order")
	private String referenceOrder;
	private String description;
	private Boolean livemode;

	@SerializedName("failure_code")
	private String failureCode;

	@SerializedName("failure_message")
	private String failureMessage;
	private String checksum;
	
	@SerializedName("redirect_url")
	private String redirectUrl;
	
	@SerializedName("approval_code")
	private String approvalCode;
	private String ref1;
	private String ref2;
	private String ref3;
	
	@SerializedName("campaign_id")
	private String campaignId;
	
	private Charge3dSource source;
	private Charge3dMpi mpi;
	
}
