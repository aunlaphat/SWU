package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExperienceTranscriptVerifyData implements Serializable {

    private static final long serialVersionUID = 1872620074756360510L;

    @Schema(description = "Verify Status")
    private Boolean verifyStatus;

    @Schema(description = "Experience Transcript Pdf Path")
    private String experienceTranscriptPdfPath;

}
