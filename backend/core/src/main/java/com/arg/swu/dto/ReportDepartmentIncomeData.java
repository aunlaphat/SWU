package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sutthiyapakc
 *
 */
@Data
public class ReportDepartmentIncomeData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = -453123123877820960L;
	
	private Date resultDate ;
	private Long depId;
	private String depNameShortTh;
	private String depNameShortEn;
	private BigDecimal remainAmount;
	private BigDecimal costShareDepPercent;
	private BigDecimal costShareDepAmount;
	private BigDecimal costShareGlobalPercent;
	private BigDecimal costShareGlobalAmount;
	private BigDecimal costShareCenterPercent;
	private BigDecimal costShareCenterAmount;	
	
	private List<Date> resultDateList;
}
