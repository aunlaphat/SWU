package com.arg.swu.dto;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
public class NewsInfoData extends PageableCommon implements Serializable {
	
    private static final long serialVersionUID = -8098525584987352173L;
    
	private Long newsId;
    private String newsHeading;
    private Long newsFormat;
    private String newsFormatNameEn;
    private String newsFormatNameTh;
    private String prefixShortTh;
    private String prefixShortEn;
    private String firstnameTh;
    private String firstnameEn;
    private String middlenameTh;
    private String middlenameEn;
    private String lastnameTh;
    private String lastnameEn;
    private Date newsStart;
    private Date newsEnd;
    private Date createDate;
    private Long createById;
    private String createdByNameTh;
    private String createdByNameEn;
    private Boolean newsStatus;
    private Long coursepublicRefId;
    private Long coursepublicRefIf;
    private String newsCoverimage;
    private String newsDetail;
    private Long newsOrder;
    private Long newsHilight;
    private Boolean newFlag;
    private Date newIconStart;
    private Date newIconEnd;
    
    private Boolean activeFlag;
    private Long newsAttachId;
    private String filename;
    private String prefix;
    private Long module;
    private String base64;
    
    @Schema(description = "custom เอกสารแนบ")
    private List<NewsInfoAttachData> newsAttachDocList;
    
}
