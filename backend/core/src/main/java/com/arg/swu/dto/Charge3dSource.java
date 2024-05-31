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
public class Charge3dSource implements Serializable {
	
	private static final long serialVersionUID = 6272735591881356413L;

	private String id;
	private String object;
	private String brand;
	
	@SerializedName("card_masking")
	private String cardMasking;
	
	@SerializedName("issuer_bank")
	private String issuerBank;
	
}
