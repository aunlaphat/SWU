package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class FinancePaymentDetail extends Footprint implements Serializable {

    private static final long serialVersionUID = -1774451246824684924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentDetailId;

    private Long paymentId;
    private String feeCode;
    private String detailNameTh;
    private String detailNameEn;
    private BigDecimal detailAmount;
    private String refTable;
    private Long refPkid;

}
