package com.arg.swu.exportor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import lombok.Builder;
import lombok.Data; 

@Data
@Builder(toBuilder = true)
public class WriteDataCell {

    private Row row;
    
    public WriteDataCell dateWrite( Date value, Integer indexCell) {
    	if(value != null)
    	{
    		Cell cell = row.createCell(indexCell);
            cell.setCellValue((new SimpleDateFormat("dd/MM/yyyy")).format(value));

            // cell.setCellValue(value);
    	}
    	return this;
    }
    
    public WriteDataCell stringWrite(String value, Integer indexCell, CellStyle hylifeStyle){
        
        if(StringUtils.isNotBlank(value))
        {
            Cell cell = row.createCell(indexCell);
            cell.setCellValue(value);
            cell.setCellStyle(hylifeStyle);
        }

        return this;
    }
    public WriteDataCell stringWrite(String value, Integer indexCell){
        
        if(StringUtils.isNotBlank(value))
        {
            Cell cell = row.createCell(indexCell);
            cell.setCellValue(value);
        }

        return this;
    }

    public WriteDataCell booleanWrite(Boolean value, Integer indexCell, CellStyle hylifeStyle){
        
        if(value != null)
        {
            Cell cell = row.createCell(indexCell);
            cell.setCellValue(value);
            cell.setCellStyle(hylifeStyle);
        }

        return this;
    }
    public WriteDataCell booleanWrite(Boolean value, Integer indexCell){
        
        if(value != null)
        {
            Cell cell = row.createCell(indexCell);
            cell.setCellValue(value);
        }

        return this;
    }
    
    public WriteDataCell booleanWriteGrade(Boolean value, Integer indexCell){
        
        if(value != null)
        {
            Cell cell = row.createCell(indexCell);
            if(value == true){
            	cell.setCellValue("Pass");
            	}else {
                cell.setCellValue("Fail");
            }
        }

        return this;
    }

    public WriteDataCell doubleWrite(Long value, Integer indexCell){
        
        if(value != null)
        {
            Cell cell = row.createCell(indexCell);
            cell.setCellValue(Double.valueOf(value));
        }

        return this;
    }
    public WriteDataCell doubleWrite(Long value, Integer indexCell, CellStyle hylifeStyle){
        
        if(value != null)
        {
            Cell cell = row.createCell(indexCell);
            cell.setCellValue(Double.valueOf(value));
            cell.setCellStyle(hylifeStyle);
        }

        return this;
    }
    public WriteDataCell bigDecimalWrite(BigDecimal value, Integer indexCell){
        
        if(value != null)
        {
            Cell cell = row.createCell(indexCell);
            // cell.setCellValue(value.doubleValue());
            cell.setCellValue((new DecimalFormat("#,###,###,###,##0.00")).format(value));
        }

        return this;
    }

    public WriteDataCell bigDecimalWritePercent(BigDecimal value, Integer indexCell){
        
        if(value != null)
        {
            Cell cell = row.createCell(indexCell);

            cell.setCellValue(value.doubleValue() + "%");
        }

        return this;
    }

    public WriteDataCell dobleWrite(String colName,Map<String, Object> col, Integer indexCell)
    {
        if(col.get(colName) != null && col.get(colName).toString() != "")
        {
            row.createCell(indexCell).setCellValue(Double.valueOf(col.get(colName).toString()));
        }
        return this;
    }
    
    

    public WriteDataCell stringWrite(String colName,Map<String, Object> col, Integer indexCell)
    {
        if(col.get(colName) != null && col.get(colName).toString() != "")
        {
            row.createCell(indexCell).setCellValue(col.get(colName).toString());
        }
        return this;
    }
    public WriteDataCell stringWrite(String colName,Map<String, Object> col, Integer indexCell, CellStyle hylifeStyle)
    {
        if(col.get(colName) != null && col.get(colName).toString() != "")
        {
            Cell cell = row.createCell(indexCell);
            cell.setCellValue(col.get(colName).toString());
            cell.setCellStyle(hylifeStyle);
        }
        return this;
    }

    public WriteDataCell dobleWrite(String colName
        ,Map<String, Object> col
        
        , Integer indexCell
        , Double hylifeValue
        , CellStyle hylifeStyle)
    {

        if(hylifeValue != null && col.get(colName) != null)
        {
            Double value = Double.valueOf(col.get(colName).toString());
            if(value > hylifeValue)
            {
                Cell cell = row.createCell(indexCell);
                cell.setCellValue(Double.valueOf(col.get(colName).toString()));
                cell.setCellStyle(hylifeStyle);
            }
            else
            {
                row.createCell(indexCell).setCellValue(Double.valueOf(col.get(colName).toString()));
            }
        }
        else
        {
            row.createCell(indexCell).setCellValue(Double.valueOf(col.get(colName).toString()));
        }
        return this;
    }
    public WriteDataCell dobleWrite(String colName
        ,Map<String, Object> col
        , Integer indexCell
        , Double hylifeValue
        , CellStyle hylifeStyle
        , CellStyle defaultStyle)
    {

        if(hylifeValue != null && col.get(colName) != null)
        {
            Double value = Double.valueOf(col.get(colName).toString());
            if(value == hylifeValue)
            {
                row.createCell(indexCell).setCellValue(Double.valueOf(col.get(colName).toString()));
            }
            else if(value > hylifeValue)
            {
                Cell cell = row.createCell(indexCell);
                cell.setCellValue(Double.valueOf(col.get(colName).toString()));
                cell.setCellStyle(hylifeStyle);
            }
            else
            {
                Cell cell = row.createCell(indexCell);
                cell.setCellValue(Double.valueOf(col.get(colName).toString()));
                cell.setCellStyle(defaultStyle);
            }
        }
        else
        {
            row.createCell(indexCell).setCellValue(Double.valueOf(col.get(colName).toString()));
        }
        return this;
    }

}
