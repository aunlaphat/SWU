package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
public class SysKbank extends Footprint implements Serializable {

	private static final long serialVersionUID = 7912760932365511632L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String baseUrl;
	private String uriCard;
	private String uriQr;
	private String publicKey;
	private String secretKey;
	private String mid;
	private String tid;
	private String src;
	private String name;
	
}
