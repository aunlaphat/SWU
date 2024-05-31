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
@Builder(toBuilder =  true)
@AllArgsConstructor
@NoArgsConstructor
public class BuasriGenerateData implements Serializable {

	private static final long serialVersionUID = 1845459017175414706L;
	
	private Long memberId;
	private String buasriId;
	private String memberFirstnameEn;
	private String memberLastnameEn;
	private String memberCardno;
	private String gidnumber;
	private String m100;
	
}
