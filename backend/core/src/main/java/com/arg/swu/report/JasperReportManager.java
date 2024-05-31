package com.arg.swu.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.*;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.tool.schema.spi.Exporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;

import java.sql.Connection;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

@Getter
@Setter
public class JasperReportManager {
    protected static Logger LOG = LoggerFactory.getLogger(JasperReportManager.class);
    private static HashMap<String , JasperReport> cache = new HashMap<String , JasperReport>();   
    private String reportType;

    public String createSVGWaterMark(String text, Integer fontSize){
        return "<svg xmlns=\"http://www.w3.org/2000/svg\"  width=\"500\" height=\"800\" ><text x=\"50%\" y=\"50%\" transform=\"rotate(-55 250 400)\" style=\"dominant-baseline:central; text-anchor:middle; font-size:"+fontSize+"px; font-weight:bold; fill:#eeeeee\">"+text+"</text></svg>";
    }

   private static InputStream getResourceAsStream(String filename)
   {
       InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
       if(in != null)
       {
           if(LOG.isDebugEnabled())
           {
                LOG.debug(String.format(" Load %1$s from Context Class Loader", filename));
           }
           return in;
       }
       
       in = JasperReportManager.class.getResourceAsStream(filename);

       if(in != null)
       {
           if(LOG.isDebugEnabled())
           {
                LOG.debug(String.format(" Load %1$s from %2$s Loader", filename,JasperReportManager.class.getName()));
           }
           return in;
       }

       in = Class.class.getResourceAsStream(filename);

       if(in != null)
       {
           if(LOG.isDebugEnabled())
           {
                LOG.debug(String.format(" Load %1$s ", filename));
           }
           return in;
       }

       LOG.info(String.format("Can not  Load %1$s ", filename));
       

       return null;

   }
    
   public static JasperReport getJasperReport(String filename,String reportType) throws Exception
   {
       try
       {
            JasperReport report = cache.get(reportType+filename);
            LOG.info(" GET JASPER REPORT "+ (report == null));
            if(report == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Compile report name : %1$s",filename));
                }
                synchronized(JasperReportManager.class)
                {
                    JasperDesign jasperDesign = JRXmlLoader.load(getResourceAsStream(filename));                    
                    cache.put(filename,JasperCompileManager.compileReport(jasperDesign));
                    jasperDesign.setIgnorePagination(true);          
                    cache.put(reportType+filename,JasperCompileManager.compileReport(jasperDesign));
                }
            }
            return cache.get(reportType+filename);
       }
       catch(JRException e)
       {
           LOG.error("Error",e);
           throw new Exception(e);
       }
   }

    public byte[] exportDocx(Map<String,Object> mapData, String jasperFile) throws JRException {
        File docxForm = new File( jasperFile);
        JasperReport jasperDesign = (JasperReport) JRLoader.loadObject(docxForm);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, mapData,  new JREmptyDataSource());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRDocxExporter export = new JRDocxExporter();
        export.setExporterInput(new SimpleExporterInput(jasperPrint));
        export.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        SimpleDocxReportConfiguration config = new SimpleDocxReportConfiguration();
        export.setConfiguration(config);
        export.exportReport();
        return out.toByteArray();
    }

    public byte[] exportXlsx(JasperReport jasperReport,Map<String,Object> parameters
            , JRBeanCollectionDataSource beanColDataSource,String nameReport) throws JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[]{nameReport});
        reportConfigXLS.setDetectCellType(true);
        reportConfigXLS.setCollapseRowSpan(false);
        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();
        return out.toByteArray();
    }
    public byte[] exportPdf(Map<String,Object> mapData, String jasperFile) throws JRException {
        File pdfForm = new File( jasperFile);
        JasperReport jasperDesign = (JasperReport) JRLoader.loadObject(pdfForm);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, mapData, new JREmptyDataSource());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SimplePdfReportConfiguration reportConfigPdf = new SimplePdfReportConfiguration();
        reportConfigPdf.setSizePageToContent(true);
        reportConfigPdf.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig  = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("NPAKTB");
        //exportConfig.setEncrypted(true);
        //exportConfig.set128BitKey(true);
        exportConfig.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);

        ThaiJRPdfExporter exporter = new ThaiJRPdfExporter();
        exporter.setConfiguration(reportConfigPdf);
        exporter.setConfiguration(exportConfig);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();

        return out.toByteArray();
    }

    
    public static class JasperType<T>{
        protected Logger logger = LoggerFactory.getLogger(JasperType.class);
        private String reportDesign;
        private JasperPrint reportCompliceSuccess;

         
        
        public JasperType(String reportDesign) {
            this.reportDesign = reportDesign;
        }



        public JasperComp complice(HashMap param, List<T> datas) throws JRException
        {
            InputStream employeeReportStream = getClass().getResourceAsStream(this.reportDesign);
        
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(employeeReportStream);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, new JRBeanCollectionDataSource(datas));
            
            this.reportCompliceSuccess = jasperPrint;
            logger.info("### complice success ###");
            return (new JasperComp(this.reportCompliceSuccess));
        }

        public JasperComp compliceDatasource(HashMap param, Connection conn) throws JRException
        { 

            InputStream employeeReportStream = getClass().getResourceAsStream(this.reportDesign);
        
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(employeeReportStream);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);
            
            this.reportCompliceSuccess = jasperPrint;
            logger.info("### complice success ###");
            return (new JasperComp(this.reportCompliceSuccess));
        }
 

        
    }

    public static class JasperComp {
        protected Logger logger = LoggerFactory.getLogger(JasperComp.class);
        private JasperPrint reportCompliceSuccess;
        private byte[] reportByte;
        
        public JasperComp(JasperPrint reportCompliceSuccess) {
            this.reportCompliceSuccess = reportCompliceSuccess;
        }

        public JasperComp buildPDF() throws Exception {

            final JRPdfExporter exporter = new JRPdfExporter();;
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
    
            exporter.setExporterInput(new SimpleExporterInput(this.reportCompliceSuccess));
            exporter.exportReport();
            this.reportByte = out.toByteArray();
            logger.info("### buildPDF success ###");
            return this;
        }

        public byte[] buildPdfToByte() throws Exception {
            final JRPdfExporter exporter = new JRPdfExporter();;
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            exporter.setExporterInput(new SimpleExporterInput(this.reportCompliceSuccess));
            
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            // exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("employeeReport.pdf"));
            
            SimplePdfReportConfiguration reportConfig
            = new SimplePdfReportConfiguration();
            reportConfig.setSizePageToContent(true);
            reportConfig.setForceLineBreakPolicy(false);

            SimplePdfExporterConfiguration exportConfig
            = new SimplePdfExporterConfiguration();
            exportConfig.setMetadataAuthor("baeldung");
            exportConfig.setEncrypted(true);
            exportConfig.setAllowedPermissionsHint("PRINTING");

            exporter.setConfiguration(reportConfig);
            exporter.setConfiguration(exportConfig);

            exporter.exportReport();
            this.reportByte = out.toByteArray();

            logger.info("### buildPdfToByte success ###");
            return this.reportByte;
        }

        public byte[] buildDocxToByte() throws Exception {
 

            final JRDocxExporter exporter = new JRDocxExporter();;
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            exporter.setExporterInput(new SimpleExporterInput(this.reportCompliceSuccess));
            
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            // exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("employeeReport.pdf"));
             
            SimpleDocxReportConfiguration exportConfig = new SimpleDocxReportConfiguration();
 
            exporter.setConfiguration(exportConfig);

            exporter.exportReport();
            this.reportByte = out.toByteArray();

            logger.info("### buildPdfToByte success ###");
            return this.reportByte;
        }

        public String exportBase64()
        {
            return Base64.encodeBase64String(this.reportByte);
        }
    }
    
}
