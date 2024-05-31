package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MasCampaign extends Footprint implements Serializable {

    private static final long serialVersionUID = 375246812492982632L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;
    private String campaignCode;
    private String campaignNameTh;
    private String campaignNameEn;
    private Date startDate;
    private Date endDate;
    private Long campaignFull;
    private Long discountFormat;
    private BigDecimal discountRate;
    private Boolean maxDiscountStatus;
    private BigDecimal maxDiscountAmount;
    private BigDecimal discountAmount;
    private BigDecimal minCoursePrice;
    private Boolean allCourse;

}
