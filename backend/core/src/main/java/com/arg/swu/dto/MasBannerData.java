package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class MasBannerData implements Serializable {

	private static final long serialVersionUID = 5606921762965005265L;
	
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
