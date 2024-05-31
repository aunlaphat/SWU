package com.arg.swu.models;

import com.arg.swu.models.commons.Footprint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author sutthiyapakc
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FinancePayment extends Footprint implements Serializable {

    private static final long serialVersionUID = -2885456526804685035L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private Long bankAccountId;
    private Long paymentType;
    private Long cardType;
    private Long memberCourseId;
    private Long coursepublicId;
    private Long memberId;
    private BigDecimal paymentAmount;
    private String ref1;
    private String ref2;
    private Long paymentStatus;
    private Date receiptDate;
    private String transactionId;
    private Date transactionDatetime;
	private String transactionIp;
	private String transactionOs;
	private String transactionBrowser;
	private String transactionAgent;
	private String transactionRawData;
	private String transactionPayAccount;
	private String receiptOriginalNonCaPath;
	private String receiptOriginalCaPath;
    private String receiptCopyNonCaPath;
	private String receiptCopyCaPath;
	private String receiptNo;
	private Long detailImpId;
	private String chargeId;
	private Long campaignId;
	private Boolean memberReceiptViewFlag;
	
	// นิติบุคคล
	private String orgCode;
	private String orgName;
	private String orgAddress;
    
}
