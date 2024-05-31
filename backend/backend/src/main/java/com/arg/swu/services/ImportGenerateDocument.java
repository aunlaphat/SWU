package com.arg.swu.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arg.swu.dto.ImportFileResultData;
import com.arg.swu.dto.SendOrderSign;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.FinanceImportDetail;
import com.arg.swu.models.FinanceImportLog;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.models.MemberInfo;
import com.arg.swu.repositories.FinanceImportDetailRepository;
import com.arg.swu.repositories.FinanceImportLogRepository;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import com.arg.swu.repositories.MemberInfoRepository;

import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportGenerateDocument {
    @Value("${default.path}")
	private String defaultPath;
	@Value("${app.config.sign.url}")
	private String signUrl;
	@Value("${app.config.env.url}")
	private String envurl;

    private final JdbcTemplate jdbcTemplate;
    private final CommonService commonService;
    private final FinancePaymentRepository financePaymentRepository;
    private final MemberCourseRepository memberCourseRepository;
    private final MemberInfoRepository memberInfoRepo;
    private final FileStorageService filestorage;
    private final FinanceImportLogRepository fimpRepo;
    private final FinanceImportDetailRepository finDetailRepo;
    private final FinancePaymentRepository finPaymentRepo;
    private final PrintReceiptService printReceiptService;
 
    private final VirtualTranscriptReportService vtreport;
    private final JWTService jwtService;

    @Scheduled(cron = "0 */5 * * * *")
	@JmsListener(destination = "generateDocumentByImport?prioritizedMessages=true")    
    public void generateDocumentByImport()
    {
        log.info("##### SCHEDURE #####");
        //Select finance_import_log where imported
        List<FinanceImportLog> fimpLogs = fimpRepo.findByStatus("imported");

        
        //Loop......
        for(FinanceImportLog fil : fimpLogs)
        {   
            try {
                fil.setStatus("process generate");
                fil.setUpdateBy(fil.getCreateBy());
                fil.setUpdateDate(new Date());
                fimpRepo.save(fil);

                List<FinanceImportDetail> finDetails = finDetailRepo.findByImpIdForGen(fil.getImpId());

                List<String> fileNames = new ArrayList<>();
                for(FinanceImportDetail finDetail: finDetails)
                {
                    log.info("processing import detail id {}", finDetail.getDetailId());
                    //Gen Receipt file name member_code+couse_code+receipt_no.pdf
                    FinancePayment payment = finPaymentRepo.findByDetailImpId(finDetail.getDetailId());
                    payment.getReceiptNo();


                    //Gen Certificate file name member_code+couse_code+cer_number.pdf
                    MemberCourse mc = memberCourseRepository.findByDetailImpId(finDetail.getDetailId());    
                    
                    Assert.isTrue(mc != null, "Member Course not found find by detail import id"+ finDetail.getDetailId());
                    
                    UploadFileData upfCaEn = generateCertificateEn(mc);
                    log.info("SING PATH upfCaEn.... {} ", upfCaEn.getFullpath());
                    UploadFileData upfCaTh = generateCertificateTh(mc);
                    log.info("SING PATH upfCaTh.... {} ", upfCaTh.getFullpath());
                    // Update
                    memberCourseRepository.save(mc);

                    MemberInfo memberInfo = memberInfoRepo.findById(mc.getMemberId()).orElse(null);

                    Assert.isTrue(memberInfo != null, "Member info not found find by  id"+ finDetail.getDetailId());

                    FinancePayment finPayment = financePaymentRepository.findByDetailImpId(finDetail.getDetailId());
                    Assert.isTrue(finPayment != null, "Finance Payment not found find by detail import id"+ finDetail.getDetailId());
                    log.info("Generate receipt");                    
                    UploadFileData rcOrg = generateReceiptOrg(memberInfo, finPayment);
                    log.info("SING PATH rcOrg.... {} ", rcOrg.getFullpath());
                    log.info("Generate receipt copy");
                    UploadFileData rcOrgCopy = generateReceiptCopy(memberInfo, finPayment);
                    log.info("SING PATH rcOrgCopy.... {} ", rcOrgCopy.getFullpath());
                    //Update
                    log.info("Updatet member info");
                    finPaymentRepo.save(finPayment);
                    // memberInfoRepo.updateReceiptPathFileById(memberInfo.getMemberId(), memberInfo.getReceiptOriginalNonCaPath);
                    

                    log.info("Add List..");
                    fileNames.addAll(Arrays.asList(upfCaEn.getFullpath()
                        , upfCaTh.getFullpath()
                        , rcOrg.getFullpath()
                        , rcOrgCopy.getFullpath()
                        ));

                        finDetail.setGentPdfFlag(Boolean.TRUE);
                        finDetailRepo.save(finDetail);
                }
                

                log.info("fileNames {}", fileNames);

                List<ImportFileResultData> listFile = findByImportId(fil.getImpId());
                //zip file
                if(!listFile.isEmpty())
                {
                    log.info("File to zip count {}", fileNames.size());
                    String zipFileName = UUID.randomUUID().toString()+FilenameUtils.EXTENSION_SEPARATOR+"zip";
                    String zipLocation = zipFile(listFile, 8, zipFileName);
                    fil.setResultFileZip(zipLocation);
                }
                else
                {
                    log.warn("File to zip count not found.");
                }
                

                fil.setStatus("success");
                fil.setUpdateDate(new Date());
                fimpRepo.save(fil);
            } catch (Exception e) {
                fil.setStatus("fail");
                fil.setMessageError(e.getMessage());
                fimpRepo.save(fil);

                log.error(e.getMessage(), e);
            }
        }
            

            

        
    }

	public List<ImportFileResultData> findByImportId(Long impId){
        String sql ="""
            select fid.detail_id 
            , fid.imp_id 
            , mc.certificate_ca_path_th 
            , mc.certificate_ca_path_en 
            , fp.receipt_original_ca_path 
            , fp.receipt_copy_ca_path 
            from finance_import_detail fid 
            left join member_course mc on fid.detail_id = mc.detail_imp_id 
            left join finance_payment fp on fid.detail_id = fp.detail_imp_id 
            where fid.imp_id = ? and fid.gent_pdf_flag = true
        """;
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ImportFileResultData.class), impId);
    }

    private UploadFileData generateReceiptCopy(MemberInfo mc, FinancePayment finPayment) throws Exception
    {
        //FileName
        String fileName = finPayment.getReceiptNo()+"-copy";
 
        byte[] orgReceiptCopyByte = printReceiptService.generateCopyReceiptReport(finPayment.getPaymentId());
        UploadFileData ufdCopy = filestorage.saveByteArrayToStorage(orgReceiptCopyByte, fileName+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 8);
        finPayment.setReceiptCopyNonCaPath(ufdCopy.getFullpath());
        // @Signed
        byte[] orgReceiptCopyByteSigned = signing(orgReceiptCopyByte);
        UploadFileData ufdOrgCopyCa = filestorage.saveByteArrayToStorage(orgReceiptCopyByteSigned,
            fileName+"-signed"+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 8);
        finPayment.setReceiptCopyCaPath(ufdOrgCopyCa.getFullpath());

        return ufdOrgCopyCa;
    }   

    private UploadFileData generateReceiptOrg(MemberInfo mc, FinancePayment finPayment) throws Exception
    {
        
        //FileName
        String fileName = finPayment.getReceiptNo();
        
        
        byte[] orgReceiptByte = printReceiptService.generateOriginalReceiptReport(finPayment.getPaymentId());
        UploadFileData ufdOrg = filestorage.saveByteArrayToStorage(orgReceiptByte, fileName+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 8);
        finPayment.setReceiptOriginalNonCaPath(ufdOrg.getFullpath());

        // @Signed
        byte[] orgReceiptByteSigned = signing(orgReceiptByte);
        UploadFileData ufdOrgCa = filestorage.saveByteArrayToStorage(orgReceiptByteSigned,
            fileName+"-signed"+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 8);
        finPayment.setReceiptOriginalCaPath(ufdOrgCa.getFullpath());
        return ufdOrgCa;
        
    }

    private UploadFileData generateCertificateTh(MemberCourse mc) throws Exception
    {
        String certThToken = Base64.getEncoder().encodeToString(jwtService.generateToken(mc.getMemberCourseId(), "certificate_th").getBytes());
        log.info("  #### generateCertTh {}",mc.getMemberCourseId());
        byte[] fileCertificateThByteArray = vtreport.generateCertTh(mc.getMemberCourseId(), envurl+"/certificate-verify-th?token="+certThToken);        
        String fileNameCertificateTh = mc.getCertificateNo() + "-" + mc.getMemberCourseId()+"-th";
        log.info("  #### saveByteArrayToStorage");
        UploadFileData responseCertificateTh = filestorage.saveByteArrayToStorage(fileCertificateThByteArray,
                fileNameCertificateTh+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 3);
        mc.setCertificateNonCaPathTh(responseCertificateTh.getFullpath());
        mc.setCertificateVerifyTh(certThToken);

        // @Signed
        log.info("  #### signing");
        byte[] fileCertificateThByteArraySigned = signing(fileCertificateThByteArray);
        log.info("  #### signing saveByteArrayToStorage");
        UploadFileData responseCertificateThSigned = filestorage.saveByteArrayToStorage(fileCertificateThByteArraySigned,
                fileNameCertificateTh+"-signed"+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 3);
        mc.setCertificateCaPathTh(responseCertificateThSigned.getFullpath());
        log.info("  #### generateCertificateTh return ");
        return responseCertificateThSigned;
    }

    private UploadFileData generateCertificateEn(MemberCourse mc) throws Exception
    {
        String certEnToken = Base64.getEncoder().encodeToString(jwtService.generateToken(mc.getMemberCourseId(), "certificate_en").getBytes());
            
        byte[] fileCertificateEnByteArray = vtreport.generateCertEn(mc.getMemberCourseId(), envurl+"/certificate-verify-en?token="+certEnToken);
        String fileNameCertificateEn = mc.getCertificateNo() + "-" + mc.getMemberCourseId()+"-en";

        UploadFileData responseCertificateEn = filestorage.saveByteArrayToStorage(fileCertificateEnByteArray,
                fileNameCertificateEn+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 3);
        mc.setCertificateNonCaPathEn(responseCertificateEn.getFullpath());
        mc.setCertificateVerifyEn(certEnToken);
        mc.setGenPdfFlag(true);
        mc.setGenPdfDate(new Date());

        //  @Signed
        byte[] fileCertificateEnByteArraySigned = signing(fileCertificateEnByteArray);
        UploadFileData responseCertificateEnSigned = filestorage.saveByteArrayToStorage(fileCertificateEnByteArraySigned,
                fileNameCertificateEn+"-signed"+ FilenameUtils.EXTENSION_SEPARATOR + "pdf", 3);
        mc.setCertificateCaPathEn(responseCertificateEnSigned.getFullpath());

        return responseCertificateEnSigned;
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

        byte[] rawString = null;

        try {
            ResponseEntity<Map> resultPesponse = restTemplate.postForEntity(url, orderEntity, Map.class);
            Map<String, Object> resMap = resultPesponse.getBody();
            rawString = Base64.getDecoder().decode((String) resMap.get("entries"));
        } catch (Exception e) {
            log.warn("Slow down sleep.5 sec. come back...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            log.warn("Retry...");
            ResponseEntity<Map> resultPesponse = restTemplate.postForEntity(url, orderEntity, Map.class);
            Map<String, Object> resMap = resultPesponse.getBody();
            rawString = Base64.getDecoder().decode((String) resMap.get("entries"));

        }
		
		 
		log.info("DATA RESULT:");

		return rawString;
	}

    public String zipFile(List<ImportFileResultData> fileNames, int pathModule, String outputZipfile) throws IOException {
        log.info("zip file processing...");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				String currentYears = sdf.format(new Date());
        
        String saveDirectory = filestorage.concatnatePath(currentYears, pathModule, outputZipfile);

        File directory = new File(filestorage.concatnatePath(currentYears, pathModule, null));

        File parentFile = new File(defaultPath);

        if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
            throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
        }
        
        if (! directory.exists()){
            directory.mkdirs();
        }
        
        
        FileOutputStream fos = new FileOutputStream(saveDirectory);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (ImportFileResultData srcFile : fileNames) 
        {
            File fileCertEnToZip = new File(defaultPath+File.separator+srcFile.getCertificateCaPathEn());
            zipToWrite(zipOut, fileCertEnToZip);

            File fileCertThToZip = new File(defaultPath+File.separator+srcFile.getCertificateCaPathTh());
            zipToWrite(zipOut, fileCertThToZip);

            File fileReceiptToZip = new File(defaultPath+File.separator+srcFile.getReceiptOriginalCaPath());
            zipToWrite(zipOut, fileReceiptToZip);

            File fileReceiptCopyToZip = new File(defaultPath+File.separator+srcFile.getReceiptCopyCaPath());
            zipToWrite(zipOut, fileReceiptCopyToZip);

        }

        zipOut.close();
       
        fos.close();

        return saveDirectory.replace(defaultPath, "");
    }

    private void zipToWrite(ZipOutputStream zipOut, File file) throws IOException
    {

        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        
        fis.close();

        log.info("zip file .. {} ", file.getName());
    }
}
