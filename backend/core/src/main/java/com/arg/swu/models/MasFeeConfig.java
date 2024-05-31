package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MasFeeConfig extends Footprint implements Serializable {

    private static final long serialVersionUID = 274146899381993529L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feeId;
    
    private String feeCode;
    private String feeNameTh;
    private String feeNameEn;
    private String feeFormatCal;
    private String feeOrder;

}
