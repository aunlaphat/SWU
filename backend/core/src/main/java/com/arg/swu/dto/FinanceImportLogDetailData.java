package com.arg.swu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FinanceImportLogDetailData implements Serializable {

    private static final long serialVersionUID = 44527102791616366L;
    
    // actual field
	private Long detailId;
    private Long impId;
    private Long coursepublicId;
    private Long memberId;
    private BigDecimal paymentAmount;
	private String idcard;
	private Date receiptDate;
	private String orgCode;
	private String orgName;
	private String orgAddress;
    private String detailStatus;
    private String errorMessage;
    
    // temp
	private Long memberCourseId;
    private Long courseId;
    
}
