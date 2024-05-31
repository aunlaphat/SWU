package com.arg.swu.models;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
public class MemberCoupon extends Footprint implements Serializable {

    private static final long serialVersionUID = 375135689382992521L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberCouponId;
    
    private Long memberId;
    private Long campaignId;
    private Long memberCourseId;
    private Boolean usageStatus;
    private Date usageTimestamp;
    private Boolean expireStatus;
    private Date expireDate;

}
