package com.arg.swu.models.commons;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.AutUser;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@MappedSuperclass
@Schema(hidden = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Footprint implements Serializable, FootprintInterfaces {
	
	private static final long serialVersionUID = 2520278765968237147L;

	private Boolean activeFlag = true;

	@Schema(hidden = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "createById", referencedColumnName = "userId", nullable = true)
	private AutUser createBy;
	
	@Schema(hidden = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updateById", referencedColumnName = "userId", nullable = true)
	private AutUser updateBy;
    @Schema(hidden = true)
	private Date createDate;
	@Schema(hidden = true)
	private Date updateDate;
	
	@Schema(hidden = true)
	public Long getCreateById() {
		return null != createBy ? createBy.getUserId() : null;
	}
	@Schema(hidden = true)
	public Long getUpdateById() {
		return null != updateBy ? updateBy.getUserId() : null;
	}
	
}