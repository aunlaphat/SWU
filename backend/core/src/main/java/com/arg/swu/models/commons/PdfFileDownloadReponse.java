package com.arg.swu.models.commons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PdfFileDownloadReponse {
    private String fileName;
    private byte[] datas;
}
