package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberConsentData implements Serializable {

    private static final long serialVersionUID = 2987731096722360631L;

    @Schema(description = "รหัส PK")
    private Long memberConsentId;

    @Schema(description = "รหัสอ้างอิง member_info.member_id")
    private Long memberId;

    @Schema(description = "รหัสอ้างอิง coursepublic_id")
    private Long consentId;

    @Schema(description = "หมายเลข Version")
    private String consentVersionNo;

    @Schema(description = "ข้อมูล mas_consent_disclose.consent_disclose_id ในรูปแบบ Array")
    private int[] consentDiscloseIds;

    @Schema(description = "IP Address ที่ยืนยัน Consent")
    private String actionIp;

    @Schema(description = "ข้อมูล Browser ที่กด")
    private String actionAgent;

}
