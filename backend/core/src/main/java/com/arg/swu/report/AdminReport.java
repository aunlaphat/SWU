package com.arg.swu.report;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;

@Getter
@Setter
@Builder
@Slf4j
public class AdminReport<T> 
{
    protected static Logger LOG = LoggerFactory.getLogger(AdminReport.class);
    private HashMap params = new HashMap<>();
    private HashMap subReport = new HashMap<>();
    private String reportType;
    private String reportName;
    
    //JavaBean
    private List<T> datas;
    //Datasource connections;
    private Connection conn;

       
    // public AdminReport(AdminReportBuilder<T> builder) {
    //     this.params = builder.params;
    //     this.subReport = builder.subReport;
    //     this.reportType = builder.reportType;
    //     this.reportName = builder.reportName;
    //     this.datas = builder.datas;
    //     this.conn = builder.conn;
    // }

    public byte[] execDatasource() throws JRException, Exception
    {
        if(this.reportType != null && this.reportType.equals(JasperReportType.PDF))
        {
            //Test
            // SqlStatmeent sql = this.conn.createStatement();
            this.conn.prepareStatement("select 1 as test").executeQuery();
            return new JasperReportManager.JasperType(this.reportName)
                                    .compliceDatasource(this.params, this.conn).buildPdfToByte();
        }
        else if(this.reportType != null && this.reportType.equals(JasperReportType.DOCX))
        {
            //Test
            // SqlStatmeent sql = this.conn.createStatement();
            this.conn.prepareStatement("select 1 as test").executeQuery();
            return new JasperReportManager.JasperType(this.reportName)
                                    .compliceDatasource(this.params, this.conn).buildDocxToByte();
        }
        else  
        {
            throw new Exception("Report Type is implement.");
        }
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
        // .setType(EmbeddedDatabaseType.HSQL)
        // .addScript("classpath:employee-schema.sql")
        .build();
    }

    public AdminReport addSubReport(String subReportParam,String reportName) throws Exception{
        log.info("add sub report. "+ reportName);
        this.params.put(subReportParam
            , JasperReportManager
                .getJasperReport(reportName,this.reportType)
                );
        return this;
    }

    public byte[] execDataModel() throws JRException, Exception
    {
        if(this.reportType != null && this.reportType.equals(JasperReportType.PDF))
        {
            return new JasperReportManager.JasperType<T>(this.reportName)
                                    .complice(this.params, this.datas).buildPdfToByte();
        }
        else if(this.reportType != null && this.reportType.equals(JasperReportType.DOCX))
        {
            //Test
            // SqlStatmeent sql = this.conn.createStatement();
            return new JasperReportManager.JasperType<T>(this.reportName)
                                    .complice(this.params, this.datas).buildDocxToByte();
        }
        else  
        {
            throw new Exception("Report Type is implement.");
        }
    }

       

    

    // @Getter
    // @Setter
    // public static class AdminReportBuilder <T>{
    //     private HashMap params = new HashMap<>();
    //     private HashMap subReport = new HashMap<>();
    //     private String reportType;
    //     private String reportName;
        
    //     //JavaBean
    //     private List<T> datas;
    //     //Datasource connections;
    //     private Connection conn;

    //     public AdminReportBuilder(String reportType, String reportName) {
    //         this.reportType = reportType;
    //         this.reportName = reportName;
    //     }
    //     public AdminReportBuilder(Connection conn, String reportType, String reportName) {
    //         this.reportType = reportType;
    //         this.reportName = reportName;
    //         this.conn = conn;
    //     }
        
    //     public AdminReportBuilder addParam(String paramName, Object value){
    //         this.params.put(paramName, value);
    //         return this;
    //     }

    //     public AdminReportBuilder addSubReport(String subReportParam,String reportName) throws Exception{
    //         this.params.put(subReportParam
    //             , JasperReportManager
    //                 .getJasperReport(reportName,JasperReportType.HTML.equals(this.reportType)?JasperReportType.HTML:"")
    //                 );
    //         return this;
    //     }

    //     public AdminReportBuilder addDatas(List<T> datas)
    //     {
    //         this.datas = datas;
    //         return this;
    //     }

    //     public AdminReport<T> build()
    //     {
    //         return new AdminReport<T>(this);
    //     }
    // }

    // public static void main(String[] args) throws JRException, Exception {
    //     // List<RssOutsourceJobSecurity> datas = new ArrayList<>();
    //     // RssOutsourceJobSecurity data = new RssOutsourceJobSecurity();
    //     // data.setVendorName("Report Test");
    //     // datas.add(data);

    //     // AdminReport<RssOutsourceJobSecurity> core = new AdminReportBuilder(JasperReportType.DOCX, "/msc/ktb/npa/report/example.jrxml")
    //     //                                                 .addDatas(datas).addParam("reportDate", "20/08/2023")
    //     //                                                 .build();
    //     // byte[] result = core.execDataModel();
    //     // System.out.println(result);
    // }
}
