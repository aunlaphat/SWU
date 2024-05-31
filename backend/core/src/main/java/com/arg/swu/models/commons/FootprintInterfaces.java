package com.arg.swu.models.commons;

import java.util.Date;

import com.arg.swu.models.AutUser;

/**
 * 
 * @author sitthichaim
 *
 */
public interface FootprintInterfaces {

	public AutUser getCreateBy();
	
	public void setCreateBy(AutUser createBy);
	
	public Date getCreateDate();
	
	public void setCreateDate(Date createDate);
	
	public AutUser getUpdateBy();
	
	public void setUpdateBy(AutUser updateBy);
	
	public Date getUpdateDate();
	
	public void setUpdateDate(Date updateDate);
	
	public Boolean getActiveFlag();
	
	public void setActiveFlag(Boolean activeFlag);
	
}
