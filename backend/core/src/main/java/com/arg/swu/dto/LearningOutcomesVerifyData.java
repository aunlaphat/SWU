package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LearningOutcomesVerifyData implements Serializable {

    private static final long serialVersionUID = 1761620074756360510L;

    @Schema(description = "Verify Status")
    private Boolean verifyStatus;

    @Schema(description = "Learning Outcomes Pdf Path")
    private String learningOutcomesPdfPath;

}
