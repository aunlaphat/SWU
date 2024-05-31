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
public class MasConsentManage extends Footprint implements Serializable {

    private static final long serialVersionUID = 385246800492992632L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long consentId;
    private String formType;
    private String versionNo;
    private String formName;
    private String formDetail;
    private String formNote;
    private String acceptButton;
    private Boolean consentStatus;

}
