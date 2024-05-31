/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.services;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.BahtTextUtil;
import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.FinanceReceiptConfigData;
import com.arg.swu.dto.SendOrderSign;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.report.AdminReport;
import com.arg.swu.report.JasperReportType;
import com.arg.swu.repositories.FinancePaymentRepository;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author kumpeep
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrintReceiptService implements ApiConstant { 
    private final JdbcTemplate jdbcTemplate;
    private final CommonService commonService;
    private final FinancePaymentRepository financePaymentRepository;
    private final MemberCourseRepository memberCourseRepository;
    private final FileStorageService filestorage;

    @Value("${app.config.sign.url}")
    private String signUrl;
    @Value("${default.path}")
    private String defaultPath;
    
    @JmsListener(destination = "receiptreport?prioritizedMessages=true")
    public void recever(String message) throws Exception {
            log.info("##### MSG #####");
            log.info(message);
            String[] convert = message.split(";");
            Long paymentId = Long.valueOf(convert[0]);
            Long autUserId = Long.valueOf(convert[1]);

            if(paymentId == null)
            {
                Assert.notNull(paymentId, "Request payment id index 0");
            }
            if(autUserId == null)
            {
                Assert.notNull(autUserId, "AutUser id index 1");
            }
            
            updateRegisPayment(new AutUser(autUserId), paymentId);

    }
    
    public void updateRegisPayment(AutUser userAction, Long paymentId) throws Exception {
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
                
                String prefixName = (new Date()).getTime()+"-"+paymentId;
                String fileName = prefixName+FilenameUtils.EXTENSION_SEPARATOR+"pdf";

                UploadFileData response = filestorage.saveByteArrayToStorage(fileByteArray, fileName, 8);
                String receiptFilePath = response.getFullpath();
                financePaymentObj.setReceiptOriginalNonCaPath(receiptFilePath);


                byte [] fileCopyByteArray = generateCopyReceiptReport(paymentId);
                String prefixNameCopy = (new Date()).getTime()+"-";
                String fileNameCopy = prefixNameCopy +paymentId+FilenameUtils.EXTENSION_SEPARATOR+"pdf";
                UploadFileData responseCopy = filestorage.saveByteArrayToStorage(fileCopyByteArray, fileNameCopy, 8);
                String receiptCopyFilePath = responseCopy.getFullpath();
                financePaymentObj.setReceiptCopyNonCaPath(receiptCopyFilePath);

                /* signing */
                byte [] fileByteArraySigned = signing(fileByteArray);
                String fileNameSigned = prefixName+"signed"+FilenameUtils.EXTENSION_SEPARATOR+"pdf";
                UploadFileData responseSigned = filestorage.saveByteArrayToStorage(fileByteArraySigned, fileNameSigned, 8);
                financePaymentObj.setReceiptOriginalCaPath(responseSigned.getFullpath());

                byte [] fileCopyByteArraySigned = signing(fileCopyByteArray);
                String fileNameCopySigned = prefixNameCopy+"signed"+FilenameUtils.EXTENSION_SEPARATOR+"pdf";
                UploadFileData responseCopySigned = filestorage.saveByteArrayToStorage(fileCopyByteArraySigned, fileNameCopySigned, 8);
                financePaymentObj.setReceiptCopyCaPath(responseCopySigned.getFullpath());

                financePaymentRepository.save(financePaymentObj);
                 
        } catch (Exception e) { 
            log.error(e.getMessage(),e);
            throw e;        
        } 
    }

    public byte[] generateOriginalReceiptReport(Long paymentId) throws Exception {
        
            BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
            BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
            BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
            BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signatureReceipt.png"));
            BufferedImage holderbg = ImageIO.read(getClass().getResource("/com/arg/swu/image/holder.png"));
            
            HashMap params = new HashMap<>();
            params.put("paymentId", paymentId);
            
            StringBuilder sb = new StringBuilder();
            List<Object> param = new ArrayList<>();
            sb.append(" select fp.receipt_no, fp.receipt_date, mi.member_id ,sp.prefixname_name_th ,sp.prefixname_name_en ,sp.prefixname_short_th ,sp.prefixname_short_en ,mi.member_firstname_th ,mi.member_firstname_en, ");
            sb.append(" mi.member_lastname_th, mi.member_lastname_en, mi.member_cardno,cpm.public_name_th,cpm.public_name_en,fp.payment_amount, ");
            sb.append(" ma.address_detail , sp2.province_name_th , sp2.province_name_en, sp2.province_name_th  , sp2.province_name_en, st.tambon_name_th  , st.tambon_name_en, ma.address_zipcode, fp.payment_amount , ");
            sb.append(" fp.payment_status, fp.payment_id,fp.payment_status,fp.org_code,fp.org_name,fp.org_address ");
            sb.append(" from finance_payment fp ");
            sb.append(" join member_info mi on fp.member_id=mi.member_id ");
            sb.append(" left join member_address ma on ma.member_id = mi.member_id and ma.address_type ='30031002' ");
            sb.append(" join coursepublic_main cpm on cpm.coursepublic_id = fp.coursepublic_id ");
            sb.append(" left join sys_province sp2 on sp2.province_id = ma.address_province ");
            sb.append(" left join sys_amphur sa  on sa.amphur_id  = ma.address_amphur ");
            sb.append(" left join sys_tambon st  on st.tambon_id = ma.address_tambon ");
            sb.append(" left join sys_prefixname sp on sp.prefixname_id = mi.prefixname_id ");
            sb.append(" where fp.payment_id=?  and fp.payment_status ='30033002' ");
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

    }
    
    public byte[] generateCopyReceiptReport(Long paymentId) throws Exception {
        
            BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
            BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
            BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
            BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signatureReceipt.png"));
            BufferedImage holderbg = ImageIO.read(getClass().getResource("/com/arg/swu/image/holder.png"));
            
            HashMap params = new HashMap<>();
            params.put("paymentId", paymentId);
            
            StringBuilder sb = new StringBuilder();
            List<Object> param = new ArrayList<>();
            sb.append(" select fp.receipt_no, fp.receipt_date, mi.member_id ,sp.prefixname_name_th ,sp.prefixname_name_en ,sp.prefixname_short_th ,sp.prefixname_short_en ,mi.member_firstname_th ,mi.member_firstname_en, ");
            sb.append(" mi.member_lastname_th, mi.member_lastname_en, mi.member_cardno,cpm.public_name_th,cpm.public_name_en,fp.payment_amount, ");
            sb.append(" ma.address_detail , sp2.province_name_th , sp2.province_name_en, sp2.province_name_th  , sp2.province_name_en, st.tambon_name_th  , st.tambon_name_en, ma.address_zipcode, fp.payment_amount , ");
            sb.append(" fp.payment_status, fp.payment_id,fp.payment_status,fp.org_code,fp.org_name,fp.org_address ");
            sb.append(" from finance_payment fp ");
            sb.append(" join member_info mi on fp.member_id=mi.member_id ");
            sb.append(" left join member_address ma on ma.member_id = mi.member_id and ma.address_type ='30031002' ");
            sb.append(" join coursepublic_main cpm on cpm.coursepublic_id = fp.coursepublic_id ");
            sb.append(" left join sys_province sp2 on sp2.province_id = ma.address_province ");
            sb.append(" left join sys_amphur sa  on sa.amphur_id  = ma.address_amphur ");
            sb.append(" left join sys_tambon st  on st.tambon_id = ma.address_tambon ");
            sb.append(" left join sys_prefixname sp on sp.prefixname_id = mi.prefixname_id ");
            sb.append(" where fp.payment_id=?  and fp.payment_status ='30033002' ");
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
    }


    public static String getContentType(String fileName) throws IOException {
        Path path = new File(fileName).toPath();
        return Files.probeContentType(path);
    }

    public static String getContentDisposition(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        return "attachment;filename*=UTF-8''" + encodedFileName + ";";
    }

    byte [] signing(byte [] rawDataBase64)
	{
		String url = signUrl + "/digitaisingature";

		SendOrderSign dataReq = new SendOrderSign();
		dataReq.setTennantId(1L);
		dataReq.setRawBase64Pdf(Base64.getEncoder().encodeToString(rawDataBase64));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<SendOrderSign> orderEntity = new HttpEntity<SendOrderSign>(dataReq, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Map> resultPesponse = restTemplate.postForEntity(url, orderEntity, Map.class);
		Map<String, Object> resMap = resultPesponse.getBody();
		log.info("DATA RESULT:");

		return Base64.getDecoder().decode((String) resMap.get("entries"));
	}

}
