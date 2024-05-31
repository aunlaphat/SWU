package com.arg.swu.dto;

import java.io.Serializable;

import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VirtualTranscriptVerifyData implements Serializable {

    private static final long serialVersionUID = 1872620074756360510L;

    @Schema(description = "Verify Status")
    private Boolean verifyStatus;

    @Schema(description = "Virtual Transcript Pdf Path")
    private String virtualTranscriptPdfPath;

    @Schema(description = "Raw data a base64")
    private String rawData;
    @Schema(description = "Raw data a content type")
    private MediaType contentType;

}
