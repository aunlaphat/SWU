package com.arg.swu.dto;

import lombok.Data;

@Data
public class ImportFileResultData {
    private Long detailId; 
    private Long impId; 
    private String certificateCaPathTh; 
    private String certificateCaPathEn; 
    private String receiptOriginalCaPath; 
    private String receiptCopyCaPath; 
}
