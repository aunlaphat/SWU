package com.arg.swu.exportor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import com.arg.swu.exportor.handler.BodyWriteHandler;
import com.arg.swu.exportor.handler.HeaderHandler;
import com.arg.swu.exportor.handler.InterfaceNativeGenerateRowData;
import com.arg.swu.exportor.handler.SheetNameHandler;
import com.arg.swu.exportor.handler.UpdateSelfFileResultHeandler;
import com.arg.swu.exportor.handler.UpdateSelfProgressHandler;
import com.arg.swu.exportor.handler.UpdateSelfStatusHandler;

import io.jsonwebtoken.lang.Assert;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ExportDataProcess<T> {
    private JdbcTemplate jdbcTemplate;  
    
    private HeaderHandler headerHandler;
    private BodyWriteHandler bodyWriteHandler;

    private InterfaceNativeGenerateRowData rowData;
    protected UpdateSelfProgressHandler updateSelfProgressHandler;
    protected UpdateSelfFileResultHeandler updateSelfFileResultHeandler;
    protected UpdateSelfStatusHandler updateSelfStatusHandler;

    private Long transactionId;

    private SheetNameHandler sheetNameHandler;
    
    protected Integer rowAccessWindowSize = 1000;
    protected Integer limitRow= 200000;

    private byte [] outputRaw;
    

    public void export() throws Exception{

        // 10000 - the number of rows that are kept in memory until flushed out
    	if(rowAccessWindowSize ==null)
    		rowAccessWindowSize = 10000;
    	
    	if(limitRow ==null)
    		limitRow = 200000;
    	
        SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindowSize);
        // Write file
        DataResponse dataResponse = this.rowData.getData();

        SXSSFSheet sheet = workbook.createSheet();

        AtomicInteger rowIndex = new AtomicInteger();
        
        


        if(headerHandler != null)
        {
            SXSSFRow rowHeader = sheet.createRow(rowIndex.getAndIncrement());
            this.headerHandler.writeHeader(rowHeader, workbook);
        }

        if(bodyWriteHandler != null)
        {
        	this.bodyWriteHandler.writeBody(dataResponse, workbook, sheet, rowIndex);
        }
        else
        {
        	sessionOnColumn(rowData, sheet, rowIndex);
        }
        
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // File file = new File(outputPath, outputFileName);
        // FileOutputStream out = new FileOutputStream(file);
        workbook.write(output);

        this.outputRaw = output.toByteArray();

        output.flush();
        output.close();

        // dispose of temporary files backing this workbook on disk
        workbook.dispose();
        workbook.close();
    }

    private void sessionOnColumn(InterfaceNativeGenerateRowData gRowData,SXSSFSheet sheet, AtomicInteger rowIndex) throws Exception
    {
        DataResponse drs = gRowData.getData();
        List<Map<String,Object>> datas = drs.getRowMap();
        
        if(limitRow != null && datas.size() > limitRow)
        {
            throw new Exception("ERROR-101: Result more than limit");
        }
         
        datas.forEach(rowData -> {
            SXSSFRow row = sheet.createRow(rowIndex.getAndIncrement());
            // log.warn("##### Data #####");
            // System.out.println(rowData.values());
            AtomicInteger columnIndex = new AtomicInteger();
            SXSSFCell cellNo = row.createCell(columnIndex.getAndIncrement());
            cellNo.setCellValue(row.getRowNum());

            rowData.values().forEach(columnData -> {
                SXSSFCell cell = row.createCell(columnIndex.getAndIncrement());
                if(columnData != null)
                cell.setCellValue(columnData.toString());                    
            });

            if ( rowIndex.get() % 1000 == 0) {                        
                log.info("Percent : " + ((Float.valueOf(rowIndex.get()) / Float.valueOf(datas.size())) * 100)+ " % ");
                if(updateSelfProgressHandler != null)
                {
                    this.updateSelfProgressHandler.updateProgress(transactionId 
                        ,((Float.valueOf(rowIndex.get()) / Float.valueOf(datas.size())) * 100));
                }                
            }
        });
    }

    public ExportDataProcess buildData(InterfaceNativeGenerateRowData generateRowData){
        Assert.notNull(generateRowData, "generateRawData cannot be null");
        this.rowData = generateRowData;
        this.headerHandler = this.rowData.writeHeader();
        this.bodyWriteHandler = this.rowData.writeBodyHandler();
        // this.updateSelfStatusHandler = this.rowData.updateStatusHandler();
        // this.updateSelfFileResultHeandler = this.rowData.updateFileResultHandler();
        // this.updateSelfProgressHandler = this.rowData.updateSelfProgressHandler();
        this.sheetNameHandler = this.rowData.sheetNameHandler();
        return this;
    }
}
