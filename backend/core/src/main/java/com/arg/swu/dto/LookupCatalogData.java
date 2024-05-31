package com.arg.swu.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LookupCatalogData implements Serializable {

	private static final long serialVersionUID = 4268542524996165741L;
	
    private Long parentId;
    private Long catalogId;
    private String key;
    private String name;
    private String nameLo;
    private String nameEn;
    private String descLo;
    private String descEn;

}
