package com.arg.swu.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author sitthichaim
 *
 */
@Data
public class MemberCouponData implements Serializable {

    private static final long serialVersionUID = 2876621985612360530L;
    
    private Long memberId;
    private Long coursepublicId;
    private Long paymentType;
    private String couponCode;

}
