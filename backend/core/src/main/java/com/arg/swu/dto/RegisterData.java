package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class RegisterData implements Serializable {

    private static final long serialVersionUID = 2997731096722360631L;

    @Schema(description = "รหัสคำนำหน้า", requiredMode = RequiredMode.REQUIRED)
    private Long prefixnameId;
    @Schema(description = "ชื่อ (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
    private String firstnameTh;
    @Schema(description = "นามสกุล (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED)
    private String lastnameTh;
    @Schema(description = "ชื่อ (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
    private String firstnameEn;
    @Schema(description = "นามสกุล (ภาษาอังกฤษ)", requiredMode = RequiredMode.REQUIRED)
    private String lastnameEn;
    @Schema(description = "อีเมล", requiredMode = RequiredMode.REQUIRED)
    private String email;
    @Schema(description = "รหัสผ่าน")
    private String password;
    @Schema(description = "Consent id")
    private Long consentId;
    @Schema(description = "Consent version no")
    private String consentVersionNo;
    @Schema(description = "Accept status")
    private Boolean acceptStatus;
    @Schema(description = "Consent disclose id")
    private int[] consentDiscloseIds;
    private String gmailToken;
    private String facebookToken;
    
}
