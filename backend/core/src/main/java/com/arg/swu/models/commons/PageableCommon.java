package com.arg.swu.models.commons;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 5210659156719545129L;
	
	private Long rowNum;
	private Integer first = 0;
    private Integer size = 5;
    
    private String mode = "search";
    
    @Schema(hidden = true)
    public Object[] getPageable() {
    	return new Object[] { getFirst(), getSize() };
    }
    
}