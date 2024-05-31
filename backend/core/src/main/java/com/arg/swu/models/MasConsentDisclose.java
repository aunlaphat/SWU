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
public class MasConsentDisclose extends Footprint implements Serializable {

    private static final long serialVersionUID = 375246800492992632L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consentDiscloseId;
    private Long consentId;
    private String consentDiscloseTh;
    private String consentDiscloseEn;

}
