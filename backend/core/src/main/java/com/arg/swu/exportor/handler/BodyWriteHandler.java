package com.arg.swu.exportor.handler;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.arg.swu.exportor.DataResponse;


public interface BodyWriteHandler<T> {
    public void writeBody(DataResponse<T> data, SXSSFWorkbook workbook, Sheet sheet, AtomicInteger rowIndex);
}
