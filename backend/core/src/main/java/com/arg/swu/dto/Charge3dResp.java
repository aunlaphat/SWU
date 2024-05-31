package com.arg.swu.dto;

import java.io.Serializable;

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
public class Charge3dResp implements Serializable {
	
	private static final long serialVersionUID = 7803643704856629335L;

	private String responseCode;
	private String responseMSG;
	
}
