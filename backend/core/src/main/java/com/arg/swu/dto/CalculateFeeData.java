package com.arg.swu.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CalculateFeeData implements Serializable {

	private static final long serialVersionUID = 2431113835998324812L;

    private Long coursepublicId;
    private Long paymentType;
    private String couponCode;
    private Long memberId;

}
