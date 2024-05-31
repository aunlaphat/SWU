/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.exportor.provider;

import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.exportor.DataResponse;
import com.arg.swu.exportor.WriteDataCell;
import com.arg.swu.exportor.handler.BodyWriteHandler;
import com.arg.swu.exportor.handler.HeaderHandler;
import com.arg.swu.exportor.handler.InterfaceNativeGenerateRowData;
import com.arg.swu.exportor.handler.SheetNameHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.modelmapper.internal.util.Assert;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author kumpeep
 */
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class PaymentDataExportProvider extends InterfaceNativeGenerateRowData<FinancePaymentData> {

    @NonNull
    private JdbcTemplate jdbcTemplate;
    @NonNull
    private String query;
    @NonNull
    private Object[] params;
    private SXSSFSheet sheet;

    private List<MemberCourseData> rawData;
    private MemberCourseData Total;

    @Override
    public DataResponse<FinancePaymentData> getData() throws Exception {

        if (rawData == null) {
            Assert.isNull(rawData, "Raw data nof found.");
        }

        return DataResponse.<FinancePaymentData>builder()
                //          .datas(rawData)
                .rowMap(jdbcTemplate.queryForList(query, params))
                .build();
    }

    private static String ifNullToEmpty(String input) {
        return StringUtils.isBlank(input) ? "" : input;
    }

    @Override
    public BodyWriteHandler writeBodyHandler() {

        return new BodyWriteHandler() {

            @Override
            public void writeBody(DataResponse data, SXSSFWorkbook workbook, Sheet sheet, AtomicInteger rowIndex) {
                // TODO Auto-generated method stub

                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setFontHeightInPoints((short) 11);
                style.setFont(font);
                List<Map<String, Object>> datas = data.getRowMap();
                for (Map<String, Object> dataReport : datas) {
                    Row row = sheet.createRow(rowIndex.getAndIncrement());

                    AtomicInteger columnIndex = new AtomicInteger();
                    
                    Cell cellNo = row.createCell(columnIndex.getAndIncrement());
                    cellNo.setCellValue(row.getRowNum());
                    WriteDataCell wt = WriteDataCell.builder().row(row).build();
                    wt.dateWrite((Date) dataReport.get("receipt_date"), columnIndex.getAndIncrement())
                            .stringWrite((String) dataReport.get("payment_type_th"), columnIndex.getAndIncrement())
                            .stringWrite((String) dataReport.get("public_name_th"), columnIndex.getAndIncrement())
                            .stringWrite((String) dataReport.get("member_firstname_th"), columnIndex.getAndIncrement())
                            .stringWrite((String) dataReport.get("member_lastname_th"), columnIndex.getAndIncrement())
                            .bigDecimalWrite((BigDecimal) dataReport.get("payment_amount"), columnIndex.getAndIncrement())
                            .stringWrite((String) dataReport.get("ref1"), columnIndex.getAndIncrement())
                            .stringWrite((String) dataReport.get("ref2"), columnIndex.getAndIncrement());
                }

            }
        };
    }

    @Override
    public HeaderHandler writeHeader() {
        return new HeaderHandler() {
            @Override
            public void writeHeader(SXSSFRow headerRow, SXSSFWorkbook workbook) {
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 11);
                headerFont.setColor(IndexedColors.BLACK.getIndex());

                // Create a CellStyle with the font
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);
                headerCellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());

                List<String> headers = new ArrayList<String>(Arrays.asList("ลำดับ", "วันที่ชำระเงิน", "ประเภทการชำระเงิน","หลักสูตร",
                        "ชื่อผู้เรียน", "นามสกุลผู้เรียน", "จำนวนเงิน", "Ref1", "Ref2"));

                AtomicInteger columnIndex = new AtomicInteger();

                headers.forEach(columnData -> {
                    SXSSFCell cell = headerRow.createCell(columnIndex.getAndIncrement());

                    if (columnData != null) {
                        cell.setCellValue(columnData);
                        cell.setCellStyle(headerCellStyle);
                    }
                });
            }
        };
    }

    @Override
    public SheetNameHandler sheetNameHandler() {
        return new SheetNameHandler() {
            @Override
            public String sheetName() {
                return "Sheet1";
            }
        };
    }
}
