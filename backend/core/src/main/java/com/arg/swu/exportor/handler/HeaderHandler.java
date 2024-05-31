package com.arg.swu.exportor.handler;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import lombok.Builder;
import lombok.Data;

/**
 * HeaderHandler
 */
public interface HeaderHandler {
    public void writeHeader(SXSSFRow headerRow, SXSSFWorkbook workbook);
}
