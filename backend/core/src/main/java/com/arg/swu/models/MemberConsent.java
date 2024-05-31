package com.arg.swu.models;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MemberConsent extends Footprint implements Serializable {

    private static final long serialVersionUID = 386135790492992632L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberConsentId;
    private Long memberId;
    private Long consentId;
    private String consentVersionNo;
    private String consentDisclose;
    private Boolean acceptStatus;
    private Date actionDatetime;
    private String actionIp;
    private String actionAgent;

}
