package com.arg.swu.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MasConsentManageData implements Serializable {

    private static final long serialVersionUID = 2997731096722360631L;

    @Schema(description = "รหัส PK")
    private Long consentId;

    @Schema(description = "ประเภทฟอร์ม")
    private String formType;

    @Schema(description = "เลขเวอร์ชั่น")
    private String versionNo;

    @Schema(description = "ชื่อฟอร์ม")
    private String formName;

    @Schema(description = "ข้อความ Consent")
    private String formDetail;

    @Schema(description = "สถานะใช้งาน")
    private Boolean consentStatus;

    private List<MasConsentDiscloseData> discloseArrayObjects;
}
