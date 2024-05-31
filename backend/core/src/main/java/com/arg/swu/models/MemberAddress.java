package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
public class MemberAddress extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 5787410364911310672L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberAddressId;
	
	private Long memberId;
	private Long addressType;
	private String addressDetail;
	private Long addressProvince;
	private Long addressAmphur;
	private Long addressTambon;
	private Long addressZipcode;
	

}
