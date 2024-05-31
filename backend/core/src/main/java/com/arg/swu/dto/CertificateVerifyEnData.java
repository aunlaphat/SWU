package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CertificateVerifyEnData implements Serializable {

    private static final long serialVersionUID = 4865520075444360522L;

    @Schema(description = "Verify Status")
    private Boolean verifyStatus;

    @Schema(description = "Certificate Path En")
    private String certificateCaPathEn;

}