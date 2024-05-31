package com.arg.swu.models;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MasBanner extends Footprint implements Serializable{
	
	private static final long serialVersionUID = 708145837724127662L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bannerId;
	
	private String bannerName;
	private String bannerLink;
	private Date displayStartDate;
	private Date displayEndDate;
	private String bannerImageTh;
	private String bannerImageEn;
	private Long orderBy;
	private Boolean activeFlag;
	private String filename;
	private String prefix;
	private Long module;
	


}
