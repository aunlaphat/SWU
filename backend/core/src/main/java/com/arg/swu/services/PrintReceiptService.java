/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.services;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.BahtTextUtil;
import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.FinanceReceiptConfigData;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.models.commons.PdfFileDownloadReponse;
import com.arg.swu.report.AdminReport;
import com.arg.swu.report.JasperReportType;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.repositories.FinanceReceiptConfigRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author kumpeep
 */
@Service
@RequiredArgsConstructor
public class PrintReceiptService implements ApiConstant {

    private static final Logger LOG = LogManager.getLogger(PrintReceiptService.class);
    private final JdbcTemplate jdbcTemplate;
    private final CommonService commonService;
    private final FinancePaymentRepository financePaymentRepository;
    private final MemberCourseRepository memberCourseRepository;
    private final FileStorageService filestorage;
    @Value("${default.path}")
    private String defaultPath;
    
    public Map<String, Object> updateRegisPayment(AutUser userAction, Long paymentId) {
        try {
            Map<String, Object> result = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            List<Object> params = new ArrayList<>();
            Date presentDate = new Date();
            sb.append("select receipt_prefix from finance_receipt_config where active_flag = true limit 1");
            FinanceReceiptConfigData entries = jdbcTemplate.queryForObject(sb.toString(),
                    BeanPropertyRowMapper.newInstance(FinanceReceiptConfigData.class));
//            if (!entries.isEmpty()) {
                // String receiptNo = commonService.generateRunningNumber(RunningNumber.RECEIPT_NO);
                FinancePayment financePaymentObj = financePaymentRepository.findById(paymentId).orElse(null);
                // financePaymentObj.setReceiptNo(receiptNo);
                financePaymentObj.setPaymentId(paymentId);
                financePaymentObj.setUpdateDate(presentDate);
                financePaymentObj.setUpdateBy(userAction);
                MemberCourse memberCourse = memberCourseRepository.findById(financePaymentObj.getMemberCourseId()).orElse(null);
                memberCourse.setPaymentStatus(30017002l);
                memberCourse.setStudyStatus(30016001l);
                memberCourse.setUpdateBy(userAction);
                memberCourse.setUpdateDate(presentDate);
                memberCourseRepository.save(memberCourse);
                
                byte [] fileByteArray = generateOriginalReceiptReport(paymentId);
                
                String fileName = (new Date()).getTime()+"-"+paymentId+FilenameUtils.EXTENSION_SEPARATOR +"pdf";
                UploadFileData response = filestorage.saveByteArrayToStorage(fileByteArray, fileName, 8);
                String receiptFilePath = response.getFullpath();
                financePaymentObj.setReceiptOriginalNonCaPath(receiptFilePath);
                fileByteArray = generateCopyReceiptReport(paymentId);
                
                fileName = (new Date()).getTime()+"-"+paymentId+FilenameUtils.EXTENSION_SEPARATOR +"pdf";
                response = filestorage.saveByteArrayToStorage(fileByteArray, fileName, 8);
                receiptFilePath = response.getFullpath();
                financePaymentObj.setReceiptCopyNonCaPath(receiptFilePath);
                
                financePaymentRepository.save(financePaymentObj);
                
                return result;
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public byte[] generateOriginalReceiptReport(Long paymentId) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
            BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
            BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
            BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signatureReceipt.png"));
            BufferedImage holderbg = ImageIO.read(getClass().getResource("/com/arg/swu/image/holder.png"));
            HashMap params = new HashMap<>();
            params.put("paymentId", paymentId);
            StringBuilder sb = new StringBuilder();
            List<Object> param = new ArrayList<>();
            sb.append("select fp.receipt_no, fp.receipt_date, mi.member_id ,sp.prefixname_name_th ,sp.prefixname_name_en ,sp.prefixname_short_th ,sp.prefixname_short_en ,mi.member_firstname_th ,mi.member_firstname_en,\n" +
            "mi.member_lastname_th, mi.member_lastname_en, mi.member_cardno,cpm.public_name_th,cpm.public_name_en,fp.payment_amount,  \n" +
            "ma.address_detail , sp2.province_name_th , sp2.province_name_en, sp2.province_name_th  , sp2.province_name_en, st.tambon_name_th  , st.tambon_name_en, ma.address_zipcode, fp.payment_amount ,\n" +
            "fp.payment_status, fp.payment_id,fp.payment_status,fp.org_code,fp.org_name,fp.org_address\n" +
            "from finance_payment fp  \n" +
            "join member_info mi on fp.member_id=mi.member_id\n" +
            "left join member_address ma on ma.member_id = mi.member_id and ma.address_type ='30031002'\n" +
            "join coursepublic_main cpm on cpm.coursepublic_id = fp.coursepublic_id \n" +
            "left join sys_province sp2 on sp2.province_id = ma.address_province \n" +
            "left join sys_amphur sa  on sa.amphur_id  = ma.address_amphur \n" +
            "left join sys_tambon st  on st.tambon_id = ma.address_tambon\n" +
            "left join sys_prefixname sp on sp.prefixname_id = mi.prefixname_id \n" +
            "where fp.payment_id=?  and fp.payment_status ='30033002'");
            param.add(paymentId);
            List<FinancePaymentData> financePaymentData = jdbcTemplate.query(sb.toString(),BeanPropertyRowMapper.newInstance(FinancePaymentData.class), param.toArray());
            sb = new StringBuilder();
            param = new ArrayList();
            sb.append("select * from finance_receipt_config frc where frc.receipt_config_id ='1'");
            List<FinanceReceiptConfigData> financeReceiptConfigList = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(FinanceReceiptConfigData.class), param.toArray());
            
            AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
                    .reportName("/com/arg/swu/jrxml/receipt.jrxml")
                    .reportType(JasperReportType.PDF)
                    .subReport(new HashMap<>())
                    .params(params)
                    .build();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy",new java.util.Locale("th", "TH"));
            adminReport.getParams().put("lang", "th");
            adminReport.getParams().put("logo", defaultPath+financeReceiptConfigList.get(0).getLogoPath());
            adminReport.getParams().put("background", background);
            adminReport.getParams().put("backgroundBorder", backgroundBorder);
            adminReport.getParams().put("signatureImg", signatureImg);
            adminReport.getParams().put("holderbg", holderbg);
            adminReport.getParams().put("paymentAmountText", BahtTextUtil.getBathText(financePaymentData.get(0).getPaymentAmount()));
            adminReport.getParams().put("staffName", financeReceiptConfigList.get(0).getStaffName());
            adminReport.getParams().put("staffPosition", financeReceiptConfigList.get(0).getStaffPosition());
            adminReport.getParams().put("staffSignaturePath", defaultPath+financeReceiptConfigList.get(0).getStaffSignaturePath());
            adminReport.getParams().put("receiptNoteEn", financeReceiptConfigList.get(0).getReceiptNoteEn());
            adminReport.getParams().put("receiptNoteTh", financeReceiptConfigList.get(0).getReceiptNoteTh());
            adminReport.getParams().put("receiptRemark", financeReceiptConfigList.get(0).getReceiptRemark());
            adminReport.getParams().put("receiptDateFormatTh",dateFormat.format(financePaymentData.get(0).getReceiptDate()));
//            adminReport.addSubReport("subreport1", "/com/arg/swu/jrxml/collectorpart.jrxml");
//            adminReport.getParams().put("subreport1", "/com/arg/swu/jrxml/collectorpart.jrxml");
            byte[] result = adminReport.execDatasource();
//            Date presentDate = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//            String presentDateFormat = dateFormat.format(presentDate);
//            LOG.info("presentDate:format: " + presentDateFormat);
//            final String FILENAME = "tmp_receipt_" + paymentId + "_" + presentDateFormat + ".pdf";
//
//            PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//                    .fileName(FILENAME)
//                    .build();

            return result;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    
    public byte[] generateCopyReceiptReport(Long paymentId) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
            BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
            BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
            BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signatureReceipt.png"));
            BufferedImage holderbg = ImageIO.read(getClass().getResource("/com/arg/swu/image/holder.png"));
            HashMap params = new HashMap<>();
            params.put("paymentId", paymentId);
            StringBuilder sb = new StringBuilder();
            List<Object> param = new ArrayList<>();
            sb.append("select fp.receipt_no, fp.receipt_date, mi.member_id ,sp.prefixname_name_th ,sp.prefixname_name_en ,sp.prefixname_short_th ,sp.prefixname_short_en ,mi.member_firstname_th ,mi.member_firstname_en,\n" +
            "mi.member_lastname_th, mi.member_lastname_en, mi.member_cardno,cpm.public_name_th,cpm.public_name_en,fp.payment_amount,  \n" +
            "ma.address_detail , sp2.province_name_th , sp2.province_name_en, sp2.province_name_th  , sp2.province_name_en, st.tambon_name_th  , st.tambon_name_en, ma.address_zipcode, fp.payment_amount ,\n" +
            "fp.payment_status, fp.payment_id,fp.payment_status,fp.org_code,fp.org_name,fp.org_address\n" +
            "from finance_payment fp  \n" +
            "join member_info mi on fp.member_id=mi.member_id\n" +
            "left join member_address ma on ma.member_id = mi.member_id and ma.address_type ='30031002'\n" +
            "join coursepublic_main cpm on cpm.coursepublic_id = fp.coursepublic_id \n" +
            "left join sys_province sp2 on sp2.province_id = ma.address_province \n" +
            "left join sys_amphur sa  on sa.amphur_id  = ma.address_amphur \n" +
            "left join sys_tambon st  on st.tambon_id = ma.address_tambon\n" +
            "left join sys_prefixname sp on sp.prefixname_id = mi.prefixname_id \n" +
            "where fp.payment_id=?  and fp.payment_status ='30033002'");
            param.add(paymentId);
            List<FinancePaymentData> financePaymentData = jdbcTemplate.query(sb.toString(),BeanPropertyRowMapper.newInstance(FinancePaymentData.class), param.toArray());
            sb = new StringBuilder();
            param = new ArrayList();
            sb.append("select * from finance_receipt_config frc where frc.receipt_config_id ='1'");
            List<FinanceReceiptConfigData> financeReceiptConfigList = jdbcTemplate.query(sb.toString(), BeanPropertyRowMapper.newInstance(FinanceReceiptConfigData.class), param.toArray());
            
            AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
                    .reportName("/com/arg/swu/jrxml/receiptCopy.jrxml")
                    .reportType(JasperReportType.PDF)
                    .subReport(new HashMap<>())
                    .params(params)
                    .build();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy",new java.util.Locale("th", "TH"));
            adminReport.getParams().put("lang", "th");
            adminReport.getParams().put("logo", defaultPath+financeReceiptConfigList.get(0).getLogoPath());
            adminReport.getParams().put("background", background);
            adminReport.getParams().put("backgroundBorder", backgroundBorder);
            adminReport.getParams().put("signatureImg", signatureImg);
            adminReport.getParams().put("holderbg", holderbg);
            adminReport.getParams().put("paymentAmountText", BahtTextUtil.getBathText(financePaymentData.get(0).getPaymentAmount()));
            adminReport.getParams().put("staffName", financeReceiptConfigList.get(0).getStaffName());
            adminReport.getParams().put("staffPosition", financeReceiptConfigList.get(0).getStaffPosition());
            adminReport.getParams().put("staffSignaturePath", defaultPath+financeReceiptConfigList.get(0).getStaffSignaturePath());
            adminReport.getParams().put("receiptNoteEn", financeReceiptConfigList.get(0).getReceiptNoteEn());
            adminReport.getParams().put("receiptNoteTh", financeReceiptConfigList.get(0).getReceiptNoteTh());
            adminReport.getParams().put("receiptRemark", financeReceiptConfigList.get(0).getReceiptRemark());
            adminReport.getParams().put("receiptDateFormatTh",dateFormat.format(financePaymentData.get(0).getReceiptDate()));
//            adminReport.addSubReport("subreport1", "/com/arg/swu/jrxml/collectorpart.jrxml");
//            adminReport.getParams().put("subreport1", "/com/arg/swu/jrxml/collectorpart.jrxml");
            byte[] result = adminReport.execDatasource();
//            Date presentDate = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//            String presentDateFormat = dateFormat.format(presentDate);
//            LOG.info("presentDate:format: " + presentDateFormat);
//            final String FILENAME = "tmp_receipt_" + paymentId + "_" + presentDateFormat + ".pdf";
//
//            PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//                    .fileName(FILENAME)
//                    .build();

            return result;

        }  catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    public static String getContentType(String fileName) throws IOException {
        Path path = new File(fileName).toPath();
        return Files.probeContentType(path);
    }

    public static String getContentDisposition(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        return "attachment;filename*=UTF-8''" + encodedFileName + ";";
    }

}
