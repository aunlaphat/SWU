package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MasConsentDiscloseData implements Serializable {
    
    private static final long serialVersionUID = 2997731096722360631L;

    @Schema(description = "รหัส PK")
    private Long consentDiscloseId;

    @Schema(description = "ข้อความ Label Checkbox Consent ภาษาไทย")
    private String consentDiscloseTh;

    @Schema(description = "ข้อความ Label Checkbox Consent ภาษาอังกฤษ")
    private String consentDiscloseEn;

}
