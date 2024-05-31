package com.arg.swu.dto;

import java.io.Serializable;

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
public class CheckoutAdditionalData implements Serializable {

    private static final long serialVersionUID = 5942026502852959271L;
    
	private String term;
    private String mid;
    private String tid;

	@SerializedName("smartpay_id")
    private String smartpayId;
    
	@SerializedName("campaign_id")
    private String campaignId;
	
}
