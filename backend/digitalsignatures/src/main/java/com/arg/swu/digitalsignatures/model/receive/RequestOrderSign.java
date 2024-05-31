package com.arg.swu.digitalsignatures.model.receive;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class RequestOrderSign {
    private Long tennantId;
    private String rawBase64Pdf;
}
